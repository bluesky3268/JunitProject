package com.meta.junitproject.handler;

import com.meta.junitproject.dto.response.CommonRespDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> apiExceptionHandle(RuntimeException e) {

        return new ResponseEntity<>(CommonRespDto.builder()
                                        .code(2)
                                        .message(e.getMessage())
                                        .build(), HttpStatus.BAD_REQUEST);
    }


}
