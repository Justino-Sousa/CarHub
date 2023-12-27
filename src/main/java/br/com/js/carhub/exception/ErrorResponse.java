package br.com.js.carhub.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;
    private int errorCode;

    public ErrorResponse(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
