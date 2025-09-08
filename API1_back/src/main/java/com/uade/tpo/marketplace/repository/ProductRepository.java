
package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.Product;



@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.category.description =?1")
    List<Product> findByCategory(String categoryDescription);

    @Query("select p from Product p where p.name =?1")
    List<Product> findByProductByName(String productName);
}
