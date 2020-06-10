package net.nomia.catalog.web.rest;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.nomia.catalog.domain.Category;
import net.nomia.catalog.domain.Product;
import net.nomia.catalog.dto.CategoriesFilterDto;
import net.nomia.catalog.dto.CategoryDto;
import net.nomia.catalog.dto.ProductDto;
import net.nomia.catalog.mapper.CategoryMapper;
import net.nomia.catalog.repository.CategoryRepository;
import net.nomia.catalog.service.CategoryService;
import net.nomia.catalog.web.Views;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@Tag(name = "Category", description = "API for categories")
@RequestMapping("/api/v1/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryController(
            CategoryService categoryService,
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @JsonView(Views.Details.class)
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable final Long id) {
        log.trace("Get a category, id: {}", id);
        return ok(categoryService.getCategory(id));
    }

    @JsonView(Views.Public.class)
    @Operation(description = "Get categories by filter")
    @GetMapping
    public ResponseEntity<Iterable<CategoryDto>> getCategories(
            final Pageable page,
            @Valid @ModelAttribute final CategoriesFilterDto filter
    ) {
        log.trace("Get categories by filter: {}", filter);
        Iterable<Category> categories = categoryService.getCategories(page, filter);
        Iterable<CategoryDto> categoryDtos = categoryMapper.categoriesToCategoryDtos(categories);
        return ok(categoryDtos);
    }

    @JsonView(Views.Public.class)
    @Operation(description = "Create a category")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @JsonView(Views.Managed.class) @Valid @RequestBody final CategoryDto managedCategoryDto
    ) throws URISyntaxException {
        log.trace("Create a category: {}", managedCategoryDto);
        Category category = categoryService.createCategory(managedCategoryDto);
        CategoryDto categoryDto = categoryMapper.categoryToCategoryDto(category);
        return created(new URI("/api/v1/categories/" + category.getId())).body(categoryDto);
    }

    @Operation(description = "Delete a category")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final Long id) {
        log.trace("Delete a category, id: {}", id);
        categoryService.deleteCategory(id);
        return noContent().build();
    }

    @JsonView(Views.Public.class)
    @Operation(summary = "Update a category")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable final Long id,
            @JsonView(Views.Managed.class) @RequestBody final CategoryDto managedCategoryDto
    ) {
        log.trace("Update a product, id: {}, data: {}", id, managedCategoryDto);
        Category category = categoryService.updateCategory(id, managedCategoryDto);
        CategoryDto categoryDto = categoryMapper.categoryToCategoryDto(category);
        return ok(categoryDto);
    }
}
