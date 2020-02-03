package com.kys.example.controller.exception;

import com.kys.example.common.result.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 예외처리 핸들러
 * @author kody.kim
 * @since 29/01/2020
 */
@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handler(Exception e, WebRequest request){
        return handleExceptionInternal(e, Response.systemError(), null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * BindException 핸들링
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, Response.error(ex.getBindingResult()), headers, status, request);
    }
}
