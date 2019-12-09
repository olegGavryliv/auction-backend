package com.havryliv.auction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private Long productId;
  @JsonProperty("timestamp")
  private LocalDateTime date;

  private UserDTO reviewer;
  private Double rating;
  private String comment;

}
