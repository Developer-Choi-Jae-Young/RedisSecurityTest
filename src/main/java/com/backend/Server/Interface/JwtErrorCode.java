package com.backend.Server.Interface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum JwtErrorCode {
	
	JWT_REFRESH_EXPIRED(200, "JWT Refresh Token is Expired"),
	JWT_NOT_VALID(200, "Jwt Token is not valid"),
	JWT_ACCESS_NOT_VALID(200, "Jwt Access Token is not valid"),
	JWT_REFRESH_NOT_VALID(200, "Jwt Refresh Token is not valid"),
    ;
	
	private int code;
	private String name;
	
	JwtErrorCode(final int code, final String name)
	{
		this.name = name;
		this.code = code;
	}
}
