package com.abnamro.recipevault.config;

import com.abnamro.recipevault.response.ErrorResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;


@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ApiResponse(responseCode = "4xx/5xx", description = "Error")
    public ResponseEntity<ErrorResponse> handleNotFound( final ResponseStatusException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(exception.getStatusCode().value(), exception.getClass().getSimpleName(), exception.getMessage()),
                exception.getStatusCode());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleThrowable(final Throwable exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getClass().getSimpleName(), null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}