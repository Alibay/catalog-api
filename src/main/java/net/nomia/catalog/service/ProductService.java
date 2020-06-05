package net.nomia.catalog.service;

import net.nomia.catalog.dto.ManagedProductDto;
import net.nomia.catalog.dto.ProductsFilterDto;
import net.nomia.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(final ManagedProductDto productDto) {

    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public void getProducts(final ProductsFilterDto filter) {

    }
}
