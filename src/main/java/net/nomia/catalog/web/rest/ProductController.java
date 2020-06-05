package net.nomia.catalog.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.nomia.catalog.dto.ManagedProductDto;
import net.nomia.catalog.dto.ProductsFilterDto;
import net.nomia.catalog.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Tag(name = "Product", description = "API for products")
@RequestMapping("/api/products")
@RestController
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a product")
    @PostMapping
    public ResponseEntity<String> createProduct(@ModelAttribute final ManagedProductDto productDto) {
        log.trace("Create a product");
        return ok().build();
    }

    @Operation(summary = "Get products by filter")
    @GetMapping
    public ResponseEntity<List<Object>> getProducts(@ModelAttribute final ProductsFilterDto filter) {
        log.trace("Get products by filter: {}", filter);
        return ok(new ArrayList<>());
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable final Long id) {
        log.trace("Delete a product, id: {}", id);
        return noContent().build();
    }

    @Operation(summary = "Update a category")
    @PutMapping("/{id}")
    public ResponseEntity updateProduct(
            @PathVariable final Long id,
            @PathVariable final ManagedProductDto productDto
    ) {
        log.trace("Update a product, id: {}, data: {}", id, productDto);
        return ok().build();
    }
}
