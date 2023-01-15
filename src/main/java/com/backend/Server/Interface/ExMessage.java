package com.backend.Server.Interface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ExMessage {

    BUSINESS_EXCEPTION_ERROR(200, "B999", "Business Exception Error"),
    INSERT_ERROR(200, "9999", "Insert Transaction Error Exception"),
    UPDATE_ERROR(200, "9999", "Update Transaction Error Exception"),
    DELETE_ERROR(200, "9999", "Delete Transaction Error Exception"),
    MEMBER_ERROR_NOT_FOUND(200, "9999", "Member Not Found"),
    ;

    private int status;
    private String divisionCode;
    private String message;

    ExMessage(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}