package com.havryliv.auction.repository.jpa;

import com.havryliv.auction.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long productId);

    @Query("SELECT AVG(r.rating) from Review r where r.product.id=?1")
    Double getAverageRatingByProductId(Long productId);

}
