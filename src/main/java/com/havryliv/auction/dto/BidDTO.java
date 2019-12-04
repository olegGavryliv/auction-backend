package com.havryliv.auction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BidDTO {

    @ApiModelProperty(position = 0, required = true)
    @Positive(message = "{positive.size.limit}")
    private BigDecimal price;

    @NotNull(message = "{product.id.notnull}")
    private Long productId;

    private LocalDateTime time;

    private Boolean success;
}
