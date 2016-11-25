package com.ihordev.helloservice.web.utils;

import java.util.Map;

import org.springframework.http.HttpHeaders;

/**
 * This is an abstract class for creating links in HTTP Link header in {@link HttpHeaders}
 * object when result data returned to user must be discoverable.
 */
public abstract class HttpHeaderLinks
{
    protected String requestUrl;
    protected Map<String, String[]> requestParameters;

    
    public HttpHeaderLinks(String requestUrl, Map<String, String[]> requestParameters)
    {
        this.requestUrl = requestUrl;
        this.requestParameters = requestParameters;
    }

    public abstract void createHeaderLink(HttpHeaders httpHeaders);
    
}
