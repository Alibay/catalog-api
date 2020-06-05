package net.nomia.catalog.web.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "API for categories")
@RequestMapping("/api/categories")
@RestController
public class CategoryController {


}
