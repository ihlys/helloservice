package com.ihordev.helloservice.web.utils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides useful methods for URL processing.
 */
public final class UrlUtils
{
    private UrlUtils()
    {
        throw new AssertionError("UrlUtils class can not be instantiated");
    }
    
    /**
     * Returns an URL address of link from HTTP Link header specified by link relation.
     * 
     * @param   links     HTTP Link header that contains links presented as list.
     * @param   rel       relation attribute that describes link.
     * @return            link address as string.
     */
    public static String getUrlFromLinkByRel(List<String> links, String rel)
    {
        for (String link : links)
        {
            if (link.contains("rel=" + rel))
            {
                return link.substring(link.indexOf('<') + 1, link.indexOf('>'));
            }
        }

        return null;
    }

    /**
     * Returns URL parameters in a map of (key, multiple values) pairs.
     * 
     * @param   urlString     string that represents full request URL or query string.
     * @return                map that holds all request URL parameters.
     */
    public static Map<String, List<String>> getUrlParameters(String urlString)
    {
        String queryString = urlString.split("\\?")[1];
        String[] keyValues = queryString.split("&");

        Map<String, List<String>> parameters = new HashMap<>();

        for (String keyValue : keyValues)
        {
            String key = keyValue.split("=")[0];
            String value = keyValue.split("=")[1];

            List<String> paramValues = parameters.get(key);
            if (paramValues == null)
            {
                paramValues = new ArrayList<>();
            }

            paramValues.add(value);

            parameters.put(key, paramValues);
        }

        return parameters;
    }

    public static boolean checkUrl(String urlString) throws MalformedURLException, URISyntaxException
    {
        new URL(urlString).toURI();
        return true;
    }
}
