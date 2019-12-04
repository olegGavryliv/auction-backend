package com.havryliv.auction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ProductDTO {

    @ApiModelProperty(position = 0, required = true)
    private Long id;

    @ApiModelProperty(position = 1, required = true)
    @Size(min = 1, max = 50, message = "{name.size.limit}")
    @NotEmpty(message = "{name.not.empty}")
    private String name;

    @ApiModelProperty(position = 2, required = true)
    @Size(min = 1, max = 50, message = "{description.size.limit}")
    private String description;

    @ApiModelProperty(position = 3, required = true)
    @Positive(message = "{positive.size.limit}")
    private BigDecimal price;

    @ApiModelProperty(position = 4, required = true)
    private String image;

    private LocalDate expireTime;

    private UserDTO owner;

    private Double rating;

    List<ReviewDTO> reviews;
}
