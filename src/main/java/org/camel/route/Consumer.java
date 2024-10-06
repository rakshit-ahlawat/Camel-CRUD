package org.camel.route;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.camel.entity.User;
import org.camel.service.UserService;

import java.util.List;


@ApplicationScoped
public class Consumer extends RouteBuilder {

    @Inject
    UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure() throws Exception {


        from("kafka:myKafkaTopicCreate?groupId=camel&autoOffsetReset=earliest&brokers={{kafka.bootstrap.servers}}")
                .log(LoggingLevel.INFO, "Received message: ${body}")
                .process(exchange -> {
                    String message = exchange.getIn().getBody(String.class);
                    User user = objectMapper.readValue(message, User.class);

                    userService.saveUser(user);
                    exchange.getIn().setBody(user);

                });

        from("kafka:myKafkaTopicGet?groupId=camel&autoOffsetReset=earliest&brokers={{kafka.bootstrap.servers}}")
                .log(LoggingLevel.INFO,"Get all users")
                        .process(exchange -> {
                            String message = exchange.getIn().getBody(String.class);
                            List<User> user = objectMapper.readValue(message, new TypeReference<List<User>>() {});
                            exchange.getIn().setBody(user);
                        });


        from("kafka:myKafkaTopicUpdate?groupId=camel&autoOffsetReset=earliest&brokers={{kafka.bootstrap.servers}}")
                .log(LoggingLevel.INFO,"updating user")

                .process(exchange -> {
                    String message = exchange.getIn().getBody(String.class);
                    User user = objectMapper.readValue(message, User.class);
                    User updatedUser = userService.updateUser(user);
                    exchange.getIn().setBody(updatedUser);
                });


        from("kafka:myKafkaTopicDelete?groupId=camel&autoOffsetReset=earliest&brokers={{kafka.bootstrap.servers}}")
                .log(LoggingLevel.INFO,"Delete User")
                .process(exchange -> {
                    String message = exchange.getIn().getBody(String.class);
                    User user = objectMapper.readValue(message, User.class);
                    User deleteUser = userService.deleteUser(user);
                    exchange.getIn().setBody(deleteUser);

                });

    }
}
