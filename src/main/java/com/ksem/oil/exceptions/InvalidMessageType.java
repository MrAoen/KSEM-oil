package com.ksem.oil.exceptions;

public class InvalidMessageType extends RuntimeException{
    public InvalidMessageType() {
    }

    public InvalidMessageType(String message) {
        super(message);
    }
}
