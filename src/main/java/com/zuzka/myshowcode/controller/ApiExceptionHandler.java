package com.zuzka.myshowcode.controller;

import com.zuzka.myshowcode.dto.ApiResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public ApiResponse handleValidationError(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return new ApiResponse("VALIDATION_FAILED", message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleMissingParameterError(MissingServletRequestParameterException ex) {
        return new ApiResponse("MISSING_PARAMETER", ex.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleMissingElementError(Exception ex) {
        return new ApiResponse("MISSING_ELEMENT", "No item found in the database");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleDuplicateElementError(SQLIntegrityConstraintViolationException ex) {
        return new ApiResponse("DUPLICATE_ELEMENT", "Item was already inserted. Try updating it.");
    }
}
