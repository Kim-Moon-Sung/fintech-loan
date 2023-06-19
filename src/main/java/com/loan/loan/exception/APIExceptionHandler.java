package com.loan.loan.exception;

import com.loan.loan.dto.ResponseDTO;
import com.loan.loan.dto.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler extends RuntimeException {

    @ExceptionHandler(BaseException.class)
    protected ResponseDTO<ResultObject> handleBaseException(BaseException e, HttpServletRequest request, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        return new ResponseDTO<>(e);
    }
}
