package net.nomia.catalog.mapper;

import com.google.common.collect.Lists;
import net.nomia.catalog.domain.Category;
import net.nomia.catalog.dto.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.reverse;
import static java.util.stream.Collectors.toList;

@Component
public class CategoryMapper {

    public CategoryDto categoryToCategoryDto(final Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setParentId(category.getParent() == null ? null : category.getParent().getId());

        return categoryDto;
    }

    public CategoryDto categoryToDetailedCategoryDto(final Category category) {
        CategoryDto categoryDto = this.categoryToCategoryDto(category);
        List<Category> parents = new ArrayList<>();
        Category parent = category.getParent();
        while (parent != null) {
            parents.add(parent);
            parent = parent.getParent();
        }

        categoryDto.setParents(this.categoriesToCategoryDtos(reverse(parents)));

        return categoryDto;
    }

    public List<CategoryDto> categoriesToCategoryDtos(final Iterable<Category> categories) {
        return Lists.newArrayList(categories)
                .stream()
                .map(this::categoryToCategoryDto)
                .collect(toList());
    }
}
