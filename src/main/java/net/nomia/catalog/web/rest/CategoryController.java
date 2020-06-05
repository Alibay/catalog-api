package net.nomia.catalog.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.nomia.catalog.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;

@Tag(name = "Category", description = "API for categories")
@RequestMapping("/api/categories")
@RestController
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(description = "Create a category")
    @PostMapping
    public ResponseEntity createCategory() throws URISyntaxException {
        return created(new URI("/api/categories/1")).build();
    }

    @Operation(description = "Delete a category")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable final Long id) {
        log.trace("Delete a category, id: {}", id);
        return noContent().build();
    }
}
