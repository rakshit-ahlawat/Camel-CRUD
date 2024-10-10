package org.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.camel.entity.User;
import org.camel.service.UserService;

import java.util.List;

@ApplicationScoped
@Named("GetUsers")
public class GetAllUsers implements Processor {

    @Inject
    UserService userService;

    private final ObjectMapper objectMapper= new ObjectMapper();


    @Override
    public void process(Exchange exchange) throws Exception {
        List<User> users =userService.getAllUsers();
        String us = objectMapper.writeValueAsString(users);
        exchange.getIn().setBody(us);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/json");
    }
}
