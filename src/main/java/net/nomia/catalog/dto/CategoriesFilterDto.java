package net.nomia.catalog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoriesFilterDto implements Serializable {
    private static final long serialVersionUID = 4914334476559665762L;

    private String name;
    private Long parentId;
}
