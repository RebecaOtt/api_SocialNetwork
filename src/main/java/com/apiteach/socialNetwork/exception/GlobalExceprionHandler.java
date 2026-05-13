package com.apiteach.socialNetwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceprionHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionDTOReq> notFoundException (ClassNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTOReq(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<ExceptionDTOReq> handleInvalidParam(InvalidParamException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTOReq(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTOReq> handleResourceAlreadyExists(ResourceAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDTOReq(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTOReq> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTOReq(HttpStatus.NOT_FOUND, e.getMessage()));
    }


}
