package com.ihordev.helloservice.web;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ihordev.helloservice.config.TestRootConfig;
import com.ihordev.helloservice.config.TestWebConfig;
import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.model.ResourcePage;
import com.ihordev.helloservice.service.ContactService;
import com.ihordev.helloservice.web.utils.UrlUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestWebConfig.class, TestRootConfig.class })
@WebAppConfiguration
@ActiveProfiles("web-test")
public class ContactControllerTest
{

    private MockMvc mockMvc;

    @Autowired
    private ContactService mockContactService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp()
    {
        Mockito.reset(mockContactService);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnContactsThatFilteredByNameFilter() throws Exception
    {
        Contact contact1 = new Contact();
        contact1.setId(1L);
        contact1.setName("Contact A");

        Contact contact2 = new Contact();
        contact2.setId(2L);
        contact2.setName("Contact B");

        Contact contact3 = new Contact();
        contact3.setId(3L);
        contact3.setName("Contact C");
      
        String nameFilter = "^A.*$";
        int page = 1;
        int limit = 3;
        
        ResourcePage<Contact> requestPage = new ResourcePage<>(page, limit);
        
        ResourcePage<Contact> resultPage = new ResourcePage<>(requestPage);
        resultPage.setRecords(Arrays.asList(contact1, contact2, contact3));
        resultPage.setTotalRecords(5);

        when(mockContactService.findContactsUsingFilterPaginated(eq(nameFilter), eq(requestPage)))
            .thenReturn(resultPage);

        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", nameFilter)
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.page", equalTo(1)))
            .andExpect(jsonPath("$.limit", equalTo(3)))
            .andExpect(jsonPath("$.totalRecordsCount", equalTo(5)))
            .andExpect(jsonPath("$.totalPagesCount", equalTo(2)))
            .andExpect(jsonPath("$.records[0].id", equalTo(1)))
            .andExpect(jsonPath("$.records[0].name", equalTo(contact1.getName())))
            .andExpect(jsonPath("$.records[1].id", equalTo(2)))
            .andExpect(jsonPath("$.records[1].name", equalTo(contact2.getName())))
            .andExpect(jsonPath("$.records[2].id", equalTo(3)))
            .andExpect(jsonPath("$.records[2].name", equalTo(contact3.getName())))
            .andDo(print());

        verify(mockContactService, atLeastOnce()).findContactsUsingFilterPaginated(eq(nameFilter), eq(requestPage));
    }

    @Test
    public void shouldReturnBadRequestResponseIfMissingNameFilter() throws Exception
    {
        mockMvc.perform(get("/hello/contacts"))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    }

    @Test
    public void shouldReturnBadRequestResponseIfInvalidNameFilter() throws Exception
    {
        String nameFilter = "[";

        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", nameFilter))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    }
    
    @Test
    public void shouldReturnBadRequestResponseIfInvalidPage() throws Exception
    {
        String nameFilter = "^A.*$";
        int page = -1;
        
        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", nameFilter)
                .param("page", String.valueOf(page)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    } 
    
    @Test
    public void shouldReturnBadRequestResponseIfInvalidLimit() throws Exception
    {
        String nameFilter = "^A.*$";
        int limit = -1;
        
        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", nameFilter)
                .param("limit", String.valueOf(limit)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    }
    
    @Test
    public void shouldReturnBadRequestResponseIfInvalidOrder() throws Exception
    {
        String nameFilter = "^A.*$";
        String order = "UNKNOWN";
        
        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", nameFilter)
                .param("order", order))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    }
    
    @Test
    public void shouldReturnBadRequestResponseIfInvalidOrderBy() throws Exception
    {
        String nameFilter = "^A.*$";
        String orderBy = "id";
        
        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", nameFilter)
                .param("orderBy", orderBy))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    }

    @Test
    public void shouldReturnBadRequestResponseIfInvalidParamTypes() throws Exception
    {
        String nameFilter = "^A.*$";
        String limit = "text in limit parameter";
        String page = "text in page parameter";
        String order = "not an order";
        
        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", nameFilter)
                .param("limit", limit)
                .param("page", page)
                .param("order", order))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    }
    
    @Test
    public void shouldReturnBadRequestResponseIfWrongPage() throws Exception
    {
        String nameFilter = "^A.*$";
        int page = 10;
        int limit = 5;

        ResourcePage<Contact> requestPage = new ResourcePage<>(page, limit);
        
        ResourcePage<Contact> resultPage = new ResourcePage<>(requestPage);
        resultPage.setTotalRecords(5);

        when(mockContactService.findContactsUsingFilterPaginated(eq(nameFilter), eq(requestPage)))
            .thenReturn(resultPage);
        
        mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", String.valueOf(nameFilter))
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print());
    }
    
    @Test
    public void shouldDiscoverPageLinks() throws Exception
    {
        String nameFilter = "^A.*$";
        int page = 3;
        int limit = 3;

        ResourcePage<Contact> requestPage = new ResourcePage<>(page, limit);
        
        ResourcePage<Contact> resultPage = new ResourcePage<>(requestPage);
        resultPage.setTotalRecords(15);

        when(mockContactService.findContactsUsingFilterPaginated(eq(nameFilter), eq(requestPage)))
            .thenReturn(resultPage);
        
        MvcResult result = mockMvc.perform(get("/hello/contacts")
                .param("nameFilter", String.valueOf(nameFilter))
                .param("page", String.valueOf(page))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andReturn();
        
        List<String> links = result.getResponse().getHeaders(HttpHeaders.LINK);
        
        String firstPageLink = UrlUtils.getUrlFromLinkByRel(links, "first");
        String prevPageLink = UrlUtils.getUrlFromLinkByRel(links, "prev");
        String nextPageLink = UrlUtils.getUrlFromLinkByRel(links, "next");
        String lastPageLink = UrlUtils.getUrlFromLinkByRel(links, "last");
        
        Mockito.reset(mockContactService);
        
        // On production run, normally, Tomcat or another Application Server will do this work
        firstPageLink = URLDecoder.decode(firstPageLink, "UTF-8");
        prevPageLink = URLDecoder.decode(prevPageLink, "UTF-8");
        nextPageLink = URLDecoder.decode(nextPageLink, "UTF-8");
        lastPageLink = URLDecoder.decode(lastPageLink, "UTF-8");
        
        checkIfRequestSuccessfullyPerforms(firstPageLink);
        checkIfRequestSuccessfullyPerforms(prevPageLink);
        checkIfRequestSuccessfullyPerforms(nextPageLink);
        checkIfRequestSuccessfullyPerforms(lastPageLink);
    }
    
    private void checkIfRequestSuccessfullyPerforms(String urlString) throws Exception
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.reset(mockContactService);
        
        Map<String, List<String>> parameters = UrlUtils.getUrlParameters(urlString);
        String nameFilter = parameters.get("nameFilter").get(0);
        int page = Integer.parseInt(parameters.get("page").get(0));
        int limit = Integer.parseInt(parameters.get("limit").get(0));
        
        ResourcePage<Contact> requestPage = new ResourcePage<>(page, limit);
        
        ResourcePage<Contact> resultPage = new ResourcePage<>(requestPage);
        resultPage.setTotalRecords(15);

        when(mockContactService.findContactsUsingFilterPaginated(eq(nameFilter), eq(requestPage)))
            .thenReturn(resultPage);
        
        mockMvc.perform(get(urlString))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }
   
}
