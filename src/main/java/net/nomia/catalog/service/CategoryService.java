package net.nomia.catalog.service;

import com.querydsl.core.BooleanBuilder;
import net.nomia.catalog.domain.Category;
import net.nomia.catalog.domain.QCategory;
import net.nomia.catalog.dto.CategoriesFilterDto;
import net.nomia.catalog.dto.CategoryDto;
import net.nomia.catalog.mapper.CategoryMapper;
import net.nomia.catalog.repository.CategoryRepository;
import net.nomia.catalog.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;
    private final CacheManager cacheManager;

    public CategoryService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            CategoryMapper categoryMapper,
            CacheManager cacheManager
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryMapper = categoryMapper;
        this.cacheManager = cacheManager;
    }

    @CacheEvict(value = "getCategory", key = "#categoryDto.parentId")
    public Category createCategory(final CategoryDto categoryDto) {
        Category parent = null;
        if (categoryDto.getParentId() != null) {
            parent = categoryRepository
                    .findById(categoryDto.getParentId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found"));
        }

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setParent(parent);

        return categoryRepository.save(category);
    }

    @Cacheable(value = "getCategory", key = "#id")
    public CategoryDto getCategory(final Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    CategoryDto categoryDto = categoryMapper.categoryToDetailedCategoryDto(category);
                    categoryDto.setChildCount(categoryRepository.countByParentId(id));
                    categoryDto.setProductsCount(productRepository.countByCategoryId(id));
                    return categoryDto;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found"));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Iterable<Category> getCategories(
            final Pageable page,
            final CategoriesFilterDto filter
    ) {
        BooleanBuilder queryBuilder = new BooleanBuilder();

        if (filter.getParentId() == null) {
            queryBuilder.and(QCategory.category.parent.id.isNull());
        } else if (filter.getParentId() != 0) {
            queryBuilder.and(QCategory.category.parent.id.eq(filter.getParentId()));
        }

        if (!StringUtils.isBlank(filter.getName())) {
            queryBuilder.and(QCategory.category.name.like(filter.getName()));
        }

        return categoryRepository.findAll(queryBuilder, page);
    }

    @CacheEvict(value = "getCategory", key = "#id")
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(final Long id, final CategoryDto categoryDto) {
        if (Objects.equals(id, categoryDto.getParentId())) {
            throw new ResponseStatusException(BAD_REQUEST, "Circular dependencies are not allowed");
        }

        return categoryRepository.findById(id)
                .map(category -> {
                    Long parentId = category.getParent() == null ? null : category.getParent().getId();
                    if (!Objects.equals(parentId, categoryDto.getParentId())) {
                        Category parent = null;

                        if (categoryDto.getParentId() != null) {
                            parent = categoryRepository.findById(categoryDto.getParentId())
                                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Parent category not found"));

                            boolean circular = false;
                            Category currentParent = parent.getParent();
                            while (currentParent != null) {
                                if (Objects.equals(currentParent.getId(), id)) {
                                    circular = true;
                                    break;
                                }

                                currentParent = currentParent.getParent();
                            }

                            if (circular) {
                                throw new ResponseStatusException(BAD_REQUEST, "Circular dependencies are not allowed");
                            }

                            cacheManager.getCache("getCategory").evict(categoryDto.getParentId());
                        }

                        category.setParent(parent);

                        if (parentId != null) {
                            cacheManager.getCache("getCategory").evict(parentId);
                        }
                    }

                    cacheManager.getCache("getCategory").evict(id);
                    category.setName(categoryDto.getName());
                    categoryRepository.save(category);

                    return category;
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found"));
    }
}
