package com.shmigel.todo.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SqlExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException
            (ConstraintViolationException sql) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "An error occurs while executing sql query",
                sql.getSQLState()));
    }

}
