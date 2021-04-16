package com.ksem.oil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidMessage extends RuntimeException{
    public InvalidMessage() {
    }

    public InvalidMessage(String message) {
        super(message);
    }
}
