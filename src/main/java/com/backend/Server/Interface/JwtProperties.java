package com.backend.Server.Interface;

public interface JwtProperties {
    int EXPIRATION_TIME = 60000 * 60;
    int REFRETSH_EXPIRATION_TIME = 60000 * 60;
    String TOKEN_PREFIX = "Bearer";
    String HEADER_STRING = "Authorization";
    String HEADER_PREFIX = "ACCESSTOKEN";
    String REFRESH_HEADER_PREFIX = "REFRESHTOKEN";
    String EXCEPTION = "Jwt_Exception";
    String ACCESS_TOKEN = "ACCESS_TOKEN";
    String REFRESH_TOKEN = "REFRESH_TOKEN";
    String ID = "JWT_ID";
    String USERNAME = "JWT_USERNAME";
}
