package com.backend.Server.Exception;

import com.backend.Server.Interface.ExMessage;

import lombok.Builder;
import lombok.Getter;

public class BussinessException extends RuntimeException{
	@Getter
    private final ExMessage errorCode;

    @Builder
    public BussinessException(ExMessage errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
