package com.example.springtask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice(basePackages = "com.example.springtask.controller")
public class GlobalExceptionHandler {
    private static final String USE_TEMPLATE_JSON_ARRAY = "You should use json object like:{" +
            "array: ['some', 'onemore']}";

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ExceptionResponse handleAccessDeniedException(
            Exception ex) {
        System.out.println(HttpStatus.BAD_REQUEST);
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, USE_TEMPLATE_JSON_ARRAY);
    }
}

