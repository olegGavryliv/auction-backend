package com.havryliv.auction.controller;

import com.havryliv.auction.dto.ReviewDTO;
import com.havryliv.auction.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Api(tags = "reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productId}")
    @ApiOperation(value = "${ReviewController.getByIdPostId}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<List<ReviewDTO>> getByIdPostId(@PathVariable Long productId){
        return ResponseEntity.ok().body(reviewService.getAllByIdProduct(productId));
    }

    @PostMapping()
    @ApiOperation(value = "${ReviewController.addReview}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public ResponseEntity<ReviewDTO> addReview(@RequestBody ReviewDTO reviewDTO, Principal principal){
        return ResponseEntity.ok().body(reviewService.addReview(reviewDTO, principal.getName()));
    }


}
