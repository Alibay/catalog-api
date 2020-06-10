package net.nomia.catalog.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.nomia.catalog.web.Views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto implements Serializable {
    private static final long serialVersionUID = -5026468572577953293L;

    @JsonView(Views.Public.class)
    private Long id;

    @JsonView({ Views.Public.class, Views.Managed.class })
    private Long parentId;

    @JsonView({ Views.Public.class, Views.Managed.class })
    private String name;

    @JsonView(Views.Details.class)
    private Integer childCount;

    @JsonView(Views.Details.class)
    private Integer productsCount;

    @JsonView(Views.Details.class)
    private List<CategoryDto> parents = new ArrayList<>();
}
