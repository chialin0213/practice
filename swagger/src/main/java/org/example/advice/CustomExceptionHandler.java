package org.example.advice;

import org.example.constant.ReturnCode;
import org.example.vo.RespData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RespData<String> exception(Exception e){
        return RespData.fail(ReturnCode.FAIL.getCode(), e.getMessage());
    }
}
