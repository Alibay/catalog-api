package net.nomia.catalog.web.rest;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.nomia.catalog.domain.Product;
import net.nomia.catalog.dto.ProductDto;
import net.nomia.catalog.dto.ProductsFilterDto;
import net.nomia.catalog.mapper.ProductMapper;
import net.nomia.catalog.repository.ProductRepository;
import net.nomia.catalog.service.ProductService;
import net.nomia.catalog.web.Views;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@Tag(name = "Product", description = "API for products")
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductController(
            ProductService productService,
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Operation(summary = "Create a product")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody final ProductDto managedProductDto
    ) throws URISyntaxException {
        log.trace("Create a product");
        Product product = productService.createProduct(managedProductDto);
        ProductDto productDto = productMapper.productToProductDto(product);
        return created(new URI("/api/v1/products/" + product.getId())).body(productDto);
    }

    @Operation(summary = "Get products by filter")
    @GetMapping
    public ResponseEntity<Iterable<ProductDto>> getProducts(
            Pageable pageable,
            @ModelAttribute final ProductsFilterDto filter) {
        log.trace("Get products by filter: {}", filter);
        Iterable<Product> products = productService.getProducts(pageable, filter);
        Iterable<ProductDto> productDtos = productMapper.productsToProductDtos(products);
        return ok(productDtos);
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable final Long id) {
        log.trace("Delete a product, id: {}", id);
        productService.deleteProduct(id);
        return noContent().build();
    }

    @JsonView(Views.Public.class)
    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    public ResponseEntity updateProduct(
            @PathVariable final Long id,
            @JsonView(Views.Managed.class) @RequestBody final ProductDto managedProductDto
    ) {
        log.trace("Update a product, id: {}, data: {}", id, managedProductDto);
        Product product = productService.updateProduct(id, managedProductDto);
        ProductDto productDto = productMapper.productToProductDto(product);
        return ok(productDto);
    }
}
