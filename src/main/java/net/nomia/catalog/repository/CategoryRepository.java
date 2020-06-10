package net.nomia.catalog.repository;

import net.nomia.catalog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, QuerydslPredicateExecutor<Category> {

    @Query("SELECT COUNT(c.id) FROM Category c WHERE c.parent.id = ?1")
    Integer countByParentId(Long parentId);
}
