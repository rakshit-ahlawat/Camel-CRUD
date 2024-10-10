//package org.camel.route;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.enterprise.context.ApplicationScoped;
//import org.apache.camel.Exchange;
//import org.apache.camel.LoggingLevel;
//import org.apache.camel.builder.RouteBuilder;
//
//
//import org.apache.camel.model.rest.RestBindingMode;
//
//
//import org.camel.entity.User;
//import org.camel.service.UserService;
//
//import jakarta.inject.Inject;
//
//import java.util.List;
//
//@ApplicationScoped
//public class RestEndpoints extends RouteBuilder {
//
//    @Inject
//    UserService userService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Override
//    public void configure() {
//        restConfiguration()
//                .component("platform-http")
//                .bindingMode(RestBindingMode.auto)
//                .host("localhost")
//                .port(8080);
//
//        rest("/users")
//                .post("/add").type(User.class).to("direct:createUser")
//                .get().to("direct:getAllUsers")
//                .put("/update").type(User.class).to("direct:updateUser")
//                .delete("/delete").type(User.class).to("direct:deleteUser");
//
//        from("direct:createUser")
//                .log(LoggingLevel.INFO,"org.camel.route","Creating a User")
//                .log("${body}")
//                .to("kafka:myKafkaTopicCreate"); // direct  bejna h
//
//
//        from("direct:getAllUsers")
//                .log(LoggingLevel.INFO,"org.camel.route","retrieving Users")
//                .process(exchange -> {
//
//                    List<User> users = userService.getAllUsers();
//
//                    String usersJson = objectMapper.writeValueAsString(users);
//                    exchange.getIn().setBody(usersJson);
//                })
//                .log("${body}")
//                .to("kafka:myKafkaTopicGet");
//
//        from("direct:updateUser")
//                .log(LoggingLevel.INFO,"org.camel.route","Updating a User")
//                .to("kafka:myKafkaTopicUpdate");
//
//        from("direct:deleteUser")
//                .log(LoggingLevel.INFO,"org.camel.route","Deleting a User")
//                .to("kafka:myKafkaTopicDelete");
//
//    }
//}