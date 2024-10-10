package org.camel.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.camel.entity.User;
import org.camel.service.UserService;

@ApplicationScoped
@Named("CreateUser")
public class CreateUser implements Processor {

    @Inject
    UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {
        String message = exchange.getIn().getBody(String.class);
        User user = objectMapper.readValue(message, User.class);
        userService.saveUser(user);
        exchange.getIn().setBody(user);

    }
}
