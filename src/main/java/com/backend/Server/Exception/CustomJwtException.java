package com.backend.Server.Exception;

import com.backend.Server.Interface.JwtErrorCode;

import lombok.Builder;
import lombok.Getter;

public class CustomJwtException extends RuntimeException{
	@Getter
    private final JwtErrorCode errorCode;
    
    @Builder
    public CustomJwtException(JwtErrorCode errorCode) {
    	   super(errorCode.getName());
           this.errorCode = errorCode;
    }
}
