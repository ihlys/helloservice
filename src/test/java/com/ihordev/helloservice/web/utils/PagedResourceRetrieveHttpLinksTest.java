package com.ihordev.helloservice.web.utils;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpHeaders;

import com.ihordev.helloservice.model.ResourcePage;

public class PagedResourceRetrieveHttpLinksTest
{

    private static final String REQUEST_URL = "http://localhost:8080/resource";

    private static final String CUSTOM_PARAM_KEY = "custom";
    private static final String CUSTOM_PARAM_VALUE = "custom";
    
    private Map<String, String[]> prepareRequestParameters(int page, int limit)
    {
        Map<String, String[]> requestParameters = new LinkedHashMap<>();
        requestParameters.put(CUSTOM_PARAM_KEY, new String[] { CUSTOM_PARAM_VALUE });
        requestParameters.put("page", new String[] { String.valueOf(page) });
        requestParameters.put("limit", new String[] { String.valueOf(limit) });

        return requestParameters;
    }

    private ResourcePage<?> prepareResourcePage(int page, int limit, int totalRecords)
    {
        ResourcePage<?> resourcePage = new ResourcePage<>(page, limit);
        resourcePage.setTotalRecords(totalRecords);

        return resourcePage;
    }
    
    private void checkLink(int page, int limit, int totalRecords, String linkRel, int expectedPage) throws MalformedURLException, URISyntaxException
    {
        Map<String, String[]> requestParameters = prepareRequestParameters(page, limit);
        ResourcePage<?> resourcePage = prepareResourcePage(page, limit, totalRecords);

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpHeaderLinks httpHeaderLinks = new PagedResourceRetrieveHttpLinks(REQUEST_URL, requestParameters, resourcePage);
        httpHeaderLinks.createHeaderLink(httpHeaders);

        List<String> links = httpHeaders.get(HttpHeaders.LINK);

        String testedPageLink = UrlUtils.getUrlFromLinkByRel(links, linkRel);
        UrlUtils.checkUrl(testedPageLink);

        Map<String, List<String>> testedPageLinkParameters = UrlUtils.getUrlParameters(testedPageLink);
        
        assertEquals(expectedPage, Integer.parseInt(testedPageLinkParameters.get("page").get(0)));
        assertEquals(limit, Integer.parseInt(testedPageLinkParameters.get("limit").get(0)));
        assertEquals(CUSTOM_PARAM_VALUE, testedPageLinkParameters.get(CUSTOM_PARAM_KEY).get(0));
    }
    
    private int getLastPage(int limit, int totalRecords)
    {
        return (totalRecords / limit) + ((totalRecords % limit > 0) ? 1 : 0);
    }
    
    @Test
    public void shouldCreateLinkToNextAndLastPageWhenFirstIsSelected() throws MalformedURLException, URISyntaxException
    {
        int page = 1;
        int limit = 3;
        int totalRecords = 15;

        checkLink(page, limit, totalRecords, "next", page + 1);
        checkLink(page, limit, totalRecords, "last", getLastPage(limit, totalRecords));
    }
    
    @Test
    public void shouldCreatePrevNextLastLinksWhenSelectedAfterFirst() throws MalformedURLException, URISyntaxException
    {
        int page = 2;
        int limit = 3;
        int totalRecords = 15;

        checkLink(page, limit, totalRecords, "prev", page - 1);
        checkLink(page, limit, totalRecords, "next", page + 1);
        checkLink(page, limit, totalRecords, "last", getLastPage(limit, totalRecords));
    }
    
    @Test
    public void shouldCreateFirstPrevNextLastLinksWhenSelectedInTheMiddle() throws MalformedURLException, URISyntaxException
    {
        int page = 3;
        int limit = 3;
        int totalRecords = 15;

        checkLink(page, limit, totalRecords, "first", 1);
        checkLink(page, limit, totalRecords, "prev", page - 1);
        checkLink(page, limit, totalRecords, "next", page + 1);
        checkLink(page, limit, totalRecords, "last", getLastPage(limit, totalRecords));
    }
    
    @Test
    public void shouldCreateFirstPrevNextWhenSelectedBeforeLast() throws MalformedURLException, URISyntaxException
    {
        int page = 4;
        int limit = 3;
        int totalRecords = 15;

        checkLink(page, limit, totalRecords, "first", 1);
        checkLink(page, limit, totalRecords, "prev", page - 1);
        checkLink(page, limit, totalRecords, "next", page + 1);
    }
    
    @Test
    public void shouldCreateFirstPrevLinksWhenSelectedLast() throws MalformedURLException, URISyntaxException
    {
        int page = 5;
        int limit = 3;
        int totalRecords = 15;

        checkLink(page, limit, totalRecords, "first", 1);
        checkLink(page, limit, totalRecords, "prev", page - 1);
    }
}
