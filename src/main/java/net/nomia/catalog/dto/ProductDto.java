package net.nomia.catalog.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.nomia.catalog.web.Views;

import java.io.Serializable;

@Data
public class ProductDto implements Serializable {
    private static final long serialVersionUID = -5829668496607405595L;

    @JsonView(Views.Public.class)
    private Long id;

    @JsonView({ Views.Public.class, Views.Managed.class })
    private Long categoryId;

    @JsonView({ Views.Public.class, Views.Managed.class })
    private String name;
}
