package com.havryliv.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorDTO {

    private String field;

    private String messages;

    public ValidationErrorDTO(String messages) {
        this.messages = messages;
    }
}
