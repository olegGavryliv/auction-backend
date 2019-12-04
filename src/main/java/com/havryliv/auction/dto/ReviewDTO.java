package com.havryliv.auction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDTO {

  private Long id;
  private Long productId;
  @JsonProperty("timestamp")
  private LocalDateTime date;

  private UserDTO reviewer;
  private Double rating;
  private String comment;

}
