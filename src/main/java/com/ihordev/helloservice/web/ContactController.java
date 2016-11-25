package com.ihordev.helloservice.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.model.ResourcePage;
import com.ihordev.helloservice.service.ContactService;
import com.ihordev.helloservice.validation.ValidNameFilter;
import com.ihordev.helloservice.validation.ValidResourcePage;
import com.ihordev.helloservice.web.utils.HttpHeaderLinks;
import com.ihordev.helloservice.web.utils.PagedResourceRetrieveHttpLinks;


@RestController
@Validated
@RequestMapping("/hello/contacts")
public class ContactController
{   
    private static final Logger logger = LoggerFactory.getLogger(PagedResourceRetrieveHttpLinks.class);
    
    private static final String NAME_FILTER_CODE = "{contacts.NameFilter}";

    @Autowired
    private ContactService contactService;
    
    
    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ResourcePage<Contact>> contacts(@RequestParam(value = "nameFilter") 
                                                              @ValidNameFilter(message = NAME_FILTER_CODE) String nameFilter,
                                                          @ValidResourcePage(maxLimit = 10, 
                                                                             orders = {ResourcePage.Order.ASC, ResourcePage.Order.DESC}, 
                                                                             checkOrderByWithEntity = Contact.class) ResourcePage<Contact> resourcePage,
                                                          HttpServletRequest request)
    {
        logger.debug("Request parameters: nameFilter=" + nameFilter + ", resourcePage=" + resourcePage);

        ResourcePage<Contact> contactsPage = contactService.findContactsUsingFilterPaginated(nameFilter, resourcePage);
        
        if (contactsPage.getPage() > contactsPage.getTotalPagesCount())
        {
            throw new WrongResourcePageException(contactsPage.getPage(), contactsPage.getTotalPagesCount());
        }
        
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpHeaderLinks httpLinks = new PagedResourceRetrieveHttpLinks(request.getRequestURL().toString(), request.getParameterMap(), contactsPage);
        httpLinks.createHeaderLink(httpHeaders);
        
        ResponseEntity<ResourcePage<Contact>> entity = new ResponseEntity<>(contactsPage, httpHeaders, HttpStatus.OK);
        
        return entity;
    }

}
