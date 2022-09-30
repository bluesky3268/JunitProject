package com.meta.junitproject.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonRespDto<T>{
    private Integer code;
    private String message;
    private T body;

    @Builder
    public CommonRespDto(Integer code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

}
