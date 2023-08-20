package com.kspia.sscmservice.common.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(e.getMessage());
        ErrorResponse response = createErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), "Bad Request", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleServletRequestBindingException(ServletRequestBindingException e, HttpServletRequest request) {
        List<String> errors = Collections.singletonList(e.getMessage());
        ErrorResponse response = createErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), "Bad Request", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest request) {
        List<String> errors = getErrorsFromFieldErrors(e.getFieldErrors());
        ErrorResponse response =
                createErrorResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(), "Bad Request", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException e, ServerWebExchange exchange) {
        List<String> errors = getErrorsFromFieldErrors(e.getFieldErrors());
        ErrorResponse response =
                createErrorResponse(HttpStatus.BAD_REQUEST, exchange.getRequest().getPath().toString(), "Bad Request", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, ServerWebExchange exchange) {
        ErrorResponse response =
                createErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, exchange.getRequest().getPath().toString(), "Method Not Allowed", Collections.singletonList(e.getMessage()));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedRuntimeException(RuntimeException e, HttpServletRequest request) {
        ErrorResponse response = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), "런타임 예외가 발생했습니다.", Collections.singletonList(e.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e, HttpServletRequest request) {
        ErrorResponse response = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), "예외가 발생했습니다.", Collections.singletonList(e.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Error.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(Error e, HttpServletRequest request) {
        ErrorResponse response = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), "시스템 에러가 발생했습니다.", Collections.singletonList(e.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedThrow(Throwable e, HttpServletRequest request) {
        ErrorResponse response = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(), "예기치 않은 오류(Throw)가 발생했습니다.", Collections.singletonList(e.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private List<String> getErrorsFromFieldErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

    private ErrorResponse createErrorResponse(HttpStatus httpStatus, String requestUri, String error, List<String> errors) {
        return new ErrorResponse(
                httpStatus.value(),
                new Date(),
                error,
                requestUri,
                errors);
    }
}
