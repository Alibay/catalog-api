package net.nomia.catalog.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_generator")
    @SequenceGenerator(name = "products_generator", sequenceName = "products_id_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreatedDate
    @Column(name ="created_at", nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    private Category category;
}
