package com.havryliv.auction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableProductDTO {

    private List<ProductDTO> products;
    @JsonProperty("all_pages_amount")
    private Integer allPagesAmount;
    @JsonProperty("all_elements_amount")
    private Long allElementsAmount;
}
