package com.havryliv.auction.exception;

import com.havryliv.auction.constants.ExceptionMessages;
import com.havryliv.auction.dto.ValidationErrorDTO;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerController {

    @Autowired
    @Qualifier("businessMessageSource")
    private final MessageSource messageSource;

    public GlobalExceptionHandlerController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    @SuppressWarnings("Duplicates")
    public ResponseEntity handleException(Exception ex, Locale locale) {
        List<ValidationErrorDTO> details = new ArrayList<>();
        details.add(new ValidationErrorDTO(ex.getLocalizedMessage()));
        String errorMessage = messageSource.getMessage(ExceptionMessages.SOMETHING_WENT_WRONG, null, locale);
        ErrorResponse error = new ErrorResponse(errorMessage, details);
        log.error("\n status: " + HttpStatus.BAD_REQUEST + "\n message :" + Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @SuppressWarnings("Duplicates")
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Locale locale) {
        List<ValidationErrorDTO> listErrors = new ArrayList<>();
        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getDefaultMessage());
            listErrors.add(new ValidationErrorDTO(error.getField(), error.getDefaultMessage()));
        }
        String errorMessage = messageSource.getMessage(ExceptionMessages.FAIL_VALIDATION, null, locale);
        ErrorResponse error = new ErrorResponse(errorMessage, listErrors);

        log.error("\n status: " + HttpStatus.BAD_REQUEST + "\n message :" + Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(BusinessException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getParams(), locale);
        ErrorResponse error = new ErrorResponse(errorMessage, new ArrayList<>());
        log.error("\n status: " + ex.getHttpStatus() + "\n message :" + Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleCustomException(UserNotFoundException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ExceptionMessages.USER_NOT_FOUND, null, locale);

        ErrorResponse error = new ErrorResponse(errorMessage, new ArrayList<>());
        log.error("\n status: " + HttpStatus.NOT_FOUND + "\n message :" + Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ExceptionMessages.ACCESS_DENIED, null, locale);

        ErrorResponse error = new ErrorResponse(errorMessage, new ArrayList<>());
        log.error("\n status: " + HttpStatus.FORBIDDEN + "\n message :" + Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

}
