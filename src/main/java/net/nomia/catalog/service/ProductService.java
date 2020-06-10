package net.nomia.catalog.service;

import com.querydsl.core.BooleanBuilder;
import net.nomia.catalog.domain.Category;
import net.nomia.catalog.domain.Product;
import net.nomia.catalog.domain.QCategory;
import net.nomia.catalog.domain.QProduct;
import net.nomia.catalog.dto.ProductDto;
import net.nomia.catalog.dto.ProductsFilterDto;
import net.nomia.catalog.mapper.ProductMapper;
import net.nomia.catalog.repository.CategoryRepository;
import net.nomia.catalog.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CacheManager cacheManager;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper,
            CacheManager cacheManager
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.cacheManager = cacheManager;
    }

    @CacheEvict(value = "getCategory", key = "#productDto.categoryId")
    public Product createProduct(final ProductDto productDto) {
        return categoryRepository.findById(productDto.getCategoryId())
                .map(category -> {
                    final Product product = productMapper.managedProductDtoToProduct(productDto);
                    product.setCategory(category);
                    productRepository.save(product);

                    return product;
                }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Iterable<Product> getProducts(
            final Pageable page,
            final ProductsFilterDto filter
    ) {
        BooleanBuilder queryBuilder = new BooleanBuilder();

        if (!StringUtils.isBlank(filter.getName())) {
            queryBuilder.and(QProduct.product.name.like(filter.getName()));
        }

        if (filter.getCategoryId() != null) {
            queryBuilder.and(QCategory.category.id.eq(filter.getCategoryId()));
        }

        if (filter.getCreatedAtFrom() != null) {
            queryBuilder.and(QProduct.product.createdAt.after(filter.getCreatedAtFrom()));
        }

        if (filter.getCreatedAtTo() != null) {
            queryBuilder.and(QProduct.product.createdAt.before(filter.getCreatedAtTo()));
        }

        return productRepository.findAll(queryBuilder, page);
    }

    public void deleteProduct(final Long id) {
        productRepository.findById(id)
                .map(product -> {
                    cacheManager.getCache("getCategory").evict(product.getCategory().getId());
                    productRepository.deleteById(id);
                    return product;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
    }

    public Product updateProduct(final Long id, final ProductDto productDto) {
        return productRepository.findById(id)
                .map(product -> {
                    if (!Objects.equals(productDto.getCategoryId(), product.getCategory().getId())) {
                        Category category = categoryRepository.findById(productDto.getCategoryId())
                            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found"));

                        product.setCategory(category);

                        cacheManager.getCache("getCategory").evict(product.getCategory().getId());
                        cacheManager.getCache("getCategory").evict(productDto.getCategoryId());
                    }

                    product.setName(productDto.getName());
                    productRepository.save(product);

                    return product;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
    }
}
