package com.havryliv.auction.controller;

import com.havryliv.auction.dto.BidDTO;
import com.havryliv.auction.dto.PageableProductDTO;
import com.havryliv.auction.dto.ProductDTO;
import com.havryliv.auction.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/products")
@Api(tags = "products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ApiOperation(value = "${PostController.add}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<ProductDTO> addProduct(@RequestBody  @Valid ProductDTO productDTO, Principal principal){
       return ResponseEntity.ok().body(productService.addProduct(productDTO, principal.getName()));
    }

    @PostMapping("/bid")
    @ApiOperation(value = "${PostController.bid}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<BidDTO> bidProduct(@RequestBody @Valid BidDTO bidDTO, Principal principal){
        return ResponseEntity.ok().body(productService.bidProduct(bidDTO, principal.getName()));
    }

    @GetMapping("")
    @ApiOperation(value = "${PostController.getAllProducts}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<PageableProductDTO> getAllProducts(@RequestParam(name = "page") int currentPage,  @RequestParam(name = "itemsPerPageCount") int itemPerPage){
        return ResponseEntity.ok().body(productService.getAllElasticSearch(currentPage , itemPerPage));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "${PostController.getById}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok().body(productService.getById(id));
    }

    @GetMapping("/random/images")
    @ApiOperation(value = "${PostController.getById}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<List<String>> getRandom3Images(){
        return ResponseEntity.ok().body(productService.get3RandomImages());
    }

}
