package com.example.springtask.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {
    protected int statusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    protected LocalDateTime timestamp;
    protected String message;

    public ExceptionResponse(HttpStatus status,  String message) {
        statusCode = status.value();
        timestamp = LocalDateTime.now();
        this.message = message;
    }
}
