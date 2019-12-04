package com.havryliv.auction.repository;

import com.havryliv.auction.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.reviews where p.id = :id")
    Optional<Product> findByIdCustom(@Param("id") Long id);


    Page<Product> findAll(Pageable pageable);
}
