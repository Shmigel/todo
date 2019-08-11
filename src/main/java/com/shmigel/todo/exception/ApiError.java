package com.shmigel.todo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    public ApiError(String status, String message, String debugMessage) {
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.debugMessage = debugMessage;
    }
}
