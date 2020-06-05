package net.nomia.catalog.dto;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
public class ManagedProductDto implements Serializable {
    private static final long serialVersionUID = 5867959745781841748L;

    @NonNull
    private String name;
}
