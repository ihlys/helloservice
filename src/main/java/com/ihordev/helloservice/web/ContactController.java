package com.ihordev.helloservice.web;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihordev.helloservice.domain.Contact;
import com.ihordev.helloservice.service.ContactService;

@RestController
@RequestMapping("/hello/contacts")
public class ContactController
{
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Contact> contacts(@RequestParam(value = "nameFilter") final String nameFilter)
    {
        logger.debug("nameFilter param: " + nameFilter);

        try
        {
            Pattern.compile(nameFilter);
        } catch (PatternSyntaxException e)
        {
            throw new InvalidRequestParameterException(nameFilter);
        }

        return contactService.findContactsUsingFilter(nameFilter);
    }

}
