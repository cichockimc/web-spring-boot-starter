package uk.co.cichocki.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class DefaultErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handling MethodArgumentNotValidException");
        var message = ex.getBindingResult().getAllErrors().stream().map(
                err -> err.getObjectName() + ": " + err.getDefaultMessage()
        ).collect(Collectors.joining(System.lineSeparator()));
        return new ResponseEntity<>(new ErrorResponse("Validation failed", message), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Handling HttpMessageNotReadableException");
        String message = Optional.ofNullable(ex.getCause()).map(t -> t.getClass().getSimpleName()).orElse("Invalid json");

        ErrorResponse errorResponse = new ErrorResponse("Problem occurred while processing message", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
