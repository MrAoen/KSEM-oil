package com.ksem.oil.controllers.errorHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksem.oil.domain.dto.TransportMessage;
import com.ksem.oil.exceptions.InvalidMessage;
import com.ksem.oil.exceptions.InvalidMessageType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerErrorAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidMessage.class)
    public ResponseEntity<Object> invalidMessageException(Exception ex, WebRequest request) throws JsonProcessingException {

        ObjectMapper objMapper = new ObjectMapper();
        TransportMessage errMsg = new TransportMessage();
        errMsg.setMessage("Invalid message structure "+ex.getCause());
        return handleExceptionInternal(ex, objMapper.writeValueAsString(errMsg), new HttpHeaders(), HttpStatus.MULTIPLE_CHOICES, request);
    }

    @ExceptionHandler(InvalidMessageType.class)
    public ResponseEntity<Object> invalidMessageTypeException(Exception ex, WebRequest request) throws JsonProcessingException {

        ObjectMapper objMapper = new ObjectMapper();
        TransportMessage errMsg = new TransportMessage();
        errMsg.setMessage("Invalid message type "+ex.getCause());
        return handleExceptionInternal(ex, objMapper.writeValueAsString(errMsg), new HttpHeaders(), HttpStatus.MULTIPLE_CHOICES, request);
    }
}
