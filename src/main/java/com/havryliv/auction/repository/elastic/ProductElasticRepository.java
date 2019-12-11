package com.havryliv.auction.repository.elastic;

import com.havryliv.auction.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductElasticRepository extends ElasticsearchRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

}
