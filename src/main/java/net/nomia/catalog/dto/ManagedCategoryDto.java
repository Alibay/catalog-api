package net.nomia.catalog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ManagedCategoryDto implements Serializable {
    private static final long serialVersionUID = 5280906902217405380L;

    private String name;
}
