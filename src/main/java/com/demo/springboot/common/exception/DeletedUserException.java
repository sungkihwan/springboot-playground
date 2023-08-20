package com.demo.springboot.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DeletedUserException extends RuntimeException {
    public DeletedUserException(String message) {
        super(message);
    }
}
