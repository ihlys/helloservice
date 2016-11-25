package com.ihordev.helloservice.web;

import java.util.Map;

/**
 * This class represents detailed error message for clients when actual data
 * cannot be returned.
 */
public class ErrorResponse
{

    private int status;
    private Map<String, String> errors;

    public ErrorResponse(int status, Map<String, String> errors)
    {
        this.status = status;
        this.errors = errors;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Map<String, String> getErrors()
    {
        return errors;
    }

    public void setErrors(Map<String, String> errors)
    {
        this.errors = errors;
    }
}