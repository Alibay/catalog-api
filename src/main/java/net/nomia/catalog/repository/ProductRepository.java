package net.nomia.catalog.repository;

import net.nomia.catalog.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    @Query("SELECT COUNT(p.id) FROM Product p WHERE p.category.id = ?1")
    Integer countByCategoryId(Long categoryId);
}
