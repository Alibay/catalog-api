package net.nomia.catalog.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductsFilterDto implements Serializable {
    private static final long serialVersionUID = 6303354408406099875L;

    private Long categoryId;

    private String name;
}
