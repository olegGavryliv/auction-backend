package com.havryliv.auction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.havryliv.auction.entity.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    @ApiModelProperty(position = 0, required = true)
    private Long id;

    @ApiModelProperty(position = 1, required = true)
    @Size(min = 1, max = 50, message = "{username.size.limit}")
    @NotEmpty(message = "{username.not.empty}")
    private String username;
    @ApiModelProperty(position = 2)
    @Size(min = 1, max = 50, message = "{email.size.limit}")
    @Email(message = "{email.not.valid}")
    private String email;
    @ApiModelProperty(position = 3, required = true)
    @Size(min = 1,max = 50, message = "{password.size.limit}")
    @NotEmpty(message = "{password.not.empty}")
    private String password;
    @ApiModelProperty(position = 4)
    List<UserRole> roles;

    private List<ProductDTO> productDTOS;

    private List<BidDTO> bidDTOS;

}
