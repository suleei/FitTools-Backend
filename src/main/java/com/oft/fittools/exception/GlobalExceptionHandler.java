package com.oft.fittools.exception;

import com.oft.fittools.global.ResponseResult;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return ResponseResult.fail(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult duplicateKeyExceptionHandler(DuplicateKeyException ex) {
        return ResponseResult.fail("插入的字段违反唯一约束");
    }

    @ExceptionHandler(value = HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult handlerMethodValidationExceptionHandler(HandlerMethodValidationException ex) {
        return  ResponseResult.fail(ex.getAllValidationResults().get(0).getResolvableErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ResponseResult exceptionHandler(Exception e){
        return ResponseResult.fail(e.getMessage().split(":")[0]);
    }

}
