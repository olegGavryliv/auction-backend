package com.havryliv.auction.exception;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Data
public class InvalidJwtAuthenticationException extends AuthenticationException {

    private HttpStatus httpStatus;

    public InvalidJwtAuthenticationException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    private static final long serialVersionUID = 1L;

}
