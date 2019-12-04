package com.havryliv.auction.exception;

import com.havryliv.auction.dto.ValidationErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse
{
    //General error message about nature of error
    private String message;

    //Specific details in API request processing
    List<ValidationErrorDTO> details;
}
