package com.example.authservice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        ApiError apiError = new ApiError(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        logger.trace(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        logger.trace(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        logger.trace(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        logger.trace(ex.getMessage());
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        logger.trace(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(UsernameAlreadyExistsException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NotNull
            HttpMessageNotReadableException ex,
            @NotNull
            HttpHeaders headers,
            @NotNull
            HttpStatus status,
            @NotNull
            WebRequest request) {

        logger.trace(ex.getMessage());

        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }
}
