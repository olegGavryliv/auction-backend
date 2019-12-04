package com.havryliv.auction.service;

import com.havryliv.auction.dto.BidDTO;
import com.havryliv.auction.dto.PageableProductDTO;
import com.havryliv.auction.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO, String owner);

    BidDTO bidProduct(BidDTO bidDTO, String buyerName);

    PageableProductDTO getAll();

    ProductDTO getById(Long id);

    List<String> get3RandomImages();
}
