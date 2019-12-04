package com.havryliv.auction.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.havryliv.auction.constants.ExceptionMessages;
import com.havryliv.auction.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

@Component("authenticationEntryPoint")
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("businessMessageSource")
    private MessageSource messageSource;


    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();

        String errorMessage = messageSource.getMessage(ExceptionMessages.EXPIRED_OR_INVALID_TOKEN,null, locale);
        ErrorResponse error = new ErrorResponse(errorMessage, new ArrayList<>());
        log.error("\n message :" + Arrays.toString(ex.getStackTrace()));
        ResponseEntity<ErrorResponse> errorResponseResponseEntity = new ResponseEntity<>(error, HttpStatus.FORBIDDEN);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OutputStream out = response.getOutputStream();
        new ObjectMapper().writeValue(out, errorResponseResponseEntity.getBody());
        out.flush();

    }
}
