package com.user.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UserNameAlreadyExists.class)
    public ResponseEntity<ProblemDetail> handle(UserNameAlreadyExists exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Duplicate Record.");
        return ResponseEntity.of(problemDetail).build();


    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handle(UserNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("User Not Exists..");
        return ResponseEntity.of(problemDetail).build();

    }

    @ExceptionHandler(RandomUserException.class)
    public ResponseEntity<ProblemDetail> handle(RandomUserException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Data Not Found");
        return ResponseEntity.of(problemDetail).build();

    }


}
