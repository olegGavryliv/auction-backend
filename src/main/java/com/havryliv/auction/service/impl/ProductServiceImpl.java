package com.havryliv.auction.service.impl;

import com.havryliv.auction.constants.ExceptionMessages;
import com.havryliv.auction.converter.BidConverter;
import com.havryliv.auction.converter.ProductConverter;
import com.havryliv.auction.dto.BidDTO;
import com.havryliv.auction.dto.PageableProductDTO;
import com.havryliv.auction.dto.ProductDTO;
import com.havryliv.auction.entity.Bid;
import com.havryliv.auction.entity.Product;
import com.havryliv.auction.entity.User;
import com.havryliv.auction.exception.BusinessException;
import com.havryliv.auction.exception.UserNotFoundException;
import com.havryliv.auction.repository.elastic.ProductElasticRepository;
import com.havryliv.auction.repository.jpa.BidRepository;
import com.havryliv.auction.repository.jpa.ProductRepository;
import com.havryliv.auction.repository.jpa.UserRepository;
import com.havryliv.auction.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private UserRepository userRepository;

    private BidRepository bidRepository;

    private ProductElasticRepository productElasticRepository;


    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, BidRepository bidRepository, ProductElasticRepository productElasticRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.productElasticRepository = productElasticRepository;
    }

    @Caching(evict = {@CacheEvict(value = "AllProducts", allEntries = true)})// clear cache when add new product in DB
    @Override
    public ProductDTO addProduct(ProductDTO productDTO, String ownerName) {
        Product product = buildProductForSave(productDTO, ownerName);

        if (productRepository.existsByName(product.getName())) {
            throw new BusinessException(ExceptionMessages.PRODUCT_NAME_ALREADY_EXIST, HttpStatus.FORBIDDEN);
        }

        product.setImage("https://picsum.photos/900/500?random&t=" + new Random().nextInt(10000 + 10));

        Product dbProduct = productRepository.save(product);

        return ProductConverter.fromEntityToDTO(dbProduct);

    }

    @Override
    @Transactional
    public BidDTO bidProduct(BidDTO bidDTO, String bidderName) {
        User bidder = userRepository.findByUsername(bidderName)
                .orElseThrow(UserNotFoundException::new);
        Bid bid = BidConverter.fromDTOtoEntity(bidDTO);
        Product product = productRepository.findById(bidDTO.getProductId())
                .orElseThrow(() -> new BusinessException(ExceptionMessages.NOT_FOUND,
                        new String[]{"product id :" + bidDTO.getProductId().toString()}, HttpStatus.NOT_FOUND));
        bid.setBidder(bidder);
        bid.setProduct(product);
        Bid dbBid = bidRepository.save(bid);
        return BidConverter.fromEntityToDTO(dbBid);
    }

    @Cacheable(value = "AllProducts", key = "#p0")
    @Override
    public PageableProductDTO getAllElasticSearch(int currentPage, int itemPerPage) {
        Page<Product> allProducts = productElasticRepository.findAll(PageRequest.of(currentPage, itemPerPage));
        return PageableProductDTO.builder()
                .products(allProducts.stream()
                        .map(ProductConverter::fromEntityToDTO)
                        .collect(Collectors.toList()))
                .allElementsAmount(allProducts.getTotalElements())
                .allPagesAmount(allProducts.getTotalPages())
                .build();

        //from db
      /*  Page<Product> allProducts = productRepository.findAll(PageRequest.of(currentPage, itemPerPage));
        return PageableProductDTO.builder()
                .products(allProducts.stream()
                        .map(ProductConverter::fromEntityToDTO)
                        .collect(Collectors.toList()))
                .allElementsAmount(allProducts.getTotalElements())
                .allPagesAmount(allProducts.getTotalPages())
                .build();*/
    }

    @Override
    public ProductDTO getById(Long id) {
        Product dbProduct = productRepository.findByIdCustom(id)
                .orElseThrow(() -> new BusinessException(ExceptionMessages.NOT_FOUND, new String[]{"product id :" + id},
                        HttpStatus.NOT_FOUND));
        return ProductConverter.fromEntityToDTO(dbProduct);
    }

    @Cacheable(value = "Top3Products")
    @Override
    public List<String> get3RandomImages() {
        return productRepository.findTop3ByRatingGreaterThanOrderByRatingAsc(2.0).stream()
                .map(Product::getImage)
                .collect(Collectors.toList());
    }

    private Product buildProductForSave(ProductDTO productDTO, String ownerName) {
        Product product = ProductConverter.fromDTOtoEntity(productDTO);
        User productOwner = userRepository.findByUsername(ownerName)
                .orElseThrow(UserNotFoundException::new);

        product.setUser(productOwner);
        return product;
    }
}
