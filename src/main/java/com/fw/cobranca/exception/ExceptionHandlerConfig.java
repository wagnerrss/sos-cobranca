package com.fw.cobranca.exception;

import com.fw.cobranca.config.AbstractAPIService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;

@RestControllerAdvice
public class ExceptionHandlerConfig extends AbstractAPIService {

    @ExceptionHandler({UnexpectedRollbackException.class, JpaSystemException.class})
    public ResponseEntity<Boolean> errorNotFound(Exception ex){
        HttpHeaders headers = getResponseHeaders(ex);
        return new ResponseEntity(false, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ParseException.class ,ClassCastException.class})
    public ResponseEntity<Boolean> errorBadRequest(Exception ex) {
        HttpHeaders headers = getResponseHeaders(ex);
        return new ResponseEntity<>(false, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Boolean> errorDataViolation(Exception ex){
        HttpHeaders headers = getResponseHeaders(ex);
        return new ResponseEntity<>(false, headers, HttpStatus.BAD_REQUEST);
    }

}
