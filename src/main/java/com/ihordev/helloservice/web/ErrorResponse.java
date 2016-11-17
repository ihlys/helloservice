package com.ihordev.helloservice.web;

/**
 * 
 * This class represents detailed error message for clients when actual 
 * data cannot be returned.
 *
 */
public class ErrorResponse {
	
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
    
}