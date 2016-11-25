package com.ihordev.helloservice.web.utils;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import com.ihordev.helloservice.model.ResourcePage;

/**
 * This class provides functionality for creating HTTP hyper links for discoverability
 * when paged resource is retrieved. It creates links according to current page
 * selection and total pages count, for example, if first page is selected in a large set 
 * of pages, than links for next and last page will be created.
 */
public class PagedResourceRetrieveHttpLinks extends HttpHeaderLinks
{
    private static final Logger logger = LoggerFactory.getLogger(PagedResourceRetrieveHttpLinks.class);
    
    private static final String PAGE_PARAM = "page";
    
    private ResourcePage<?> resourcePage;
    
    /**
     * Constructs a new instance with specified configuration.
     * 
     * @param requestUrl          the URL that contains a protocol, server name, 
     *                            port number, server path, but it does not 
     *                            include query string parameters.
     * @param requestParameters   a map that contains request parameters from a request query string.
     * @param                     resource page that contains information for pagination
     *                            such as current selected page, total pages count.
     */
    public PagedResourceRetrieveHttpLinks(String requestUrl, Map<String, String[]> requestParameters, ResourcePage<?> resourcePage)
    {
        super(requestUrl, requestParameters);
        this.resourcePage = resourcePage;
    }

    /**
     * Creates Link header in {@link HttpHeaders} object with HTTP hyper links. Every created link
     * has relation attribute that describes link. Links are created for first, previous, next and 
     * last pages. Depending on the current selected page and total pages count some links 
     * may be omitted, for example if first or last page selected or if there is no 
     * previous page or next page.
     * 
     * @param   httpHeaders       an instance of HttpHeaders that must be populated with links.
     */
    public void createHeaderLink(HttpHeaders httpHeaders)
    {
        if (hasPrevPage())
        {
            httpHeaders.add(HttpHeaders.LINK, uriToLinkHeaderString(createPrevPageUri(), "previous"));
        }
        
        if (hasNextPage())
        {
            httpHeaders.add(HttpHeaders.LINK, uriToLinkHeaderString(createNextPageUri(), "next"));
        }
        
        if (hasFirstPage())
        {
            httpHeaders.add(HttpHeaders.LINK, uriToLinkHeaderString(createFirstPageUri(), "first"));
        }
        
        if (hasLastPage())
        {
            httpHeaders.add(HttpHeaders.LINK, uriToLinkHeaderString(createLastPageUri(), "last"));
        }
       
    }
    
    private boolean hasPrevPage()
    {
        return resourcePage.getPage() > 1;
    }
    
    private boolean hasNextPage()
    {
        return resourcePage.getPage() < resourcePage.getTotalPagesCount();
    }
    
    private boolean hasFirstPage()
    {
        return resourcePage.getPage() > 2;
    }
    
    private boolean hasLastPage()
    {
        return resourcePage.getPage() < resourcePage.getTotalPagesCount() - 1;
    }
    
    private URI createPrevPageUri()
    {
        return createPageUri(resourcePage.getPage() - 1);
    }

    private URI createNextPageUri()
    {
        return createPageUri(resourcePage.getPage() + 1);
    }
    
    private URI createFirstPageUri()
    {
        return createPageUri(1);
    }
    
    private URI createLastPageUri()
    {
        return createPageUri(resourcePage.getTotalPagesCount());
    }
    
    private URI createPageUri(int page)
    {        
        UriComponentsBuilder ucb = prepareUriBuilder();
        
        ucb.replaceQueryParam(PAGE_PARAM, page);
        
        URI uri = ucb.build().toUri();
        
        logger.debug("Generated pagination URI for Link header: " + uri);
        
        return uri;
    }
   
    private UriComponentsBuilder prepareUriBuilder()
    {
        UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(requestUrl);
        
        for (Map.Entry<String, String[]> entry : requestParameters.entrySet())
        {
            ucb.queryParam(entry.getKey(), (Object[]) entry.getValue());
        }
        
        return ucb;
    }
    
    private String uriToLinkHeaderString(URI uri, String rel)
    {
        return "<" + uri + ">; rel=" + rel;
    }
    
}
