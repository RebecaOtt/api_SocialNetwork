package com.apiteach.socialNetwork.exception;

import org.springframework.http.HttpStatus;

public record ExceptionDTOReq(HttpStatus status, String message) {
}
