package org.camel.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.camel.entity.User;
import org.camel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
@ActivateRequestContext // managed by quarkus, because it only picks greeting resourse as default
public class UserService {
    private static Logger logger= LoggerFactory.getLogger(UserService.class);


    @Inject
    UserRepository userRepository;


    public List<User> getAllUsers() {
        logger.info("class:UserService , method:getAllUsers , message= retrieving all users");

        List<User> user=userRepository.listAll();
        List<User> response=new ArrayList<>();

        for(User us:user){
            if(us.isActive()){
                response.add(us);
            }
        }
        return response;
    }

    @Transactional
    public User saveUser(User user) {
        logger.info("class:UserService , method: saveUser , message= Saving a user");
        user.setActive(true);
        userRepository.persist(user);
        return user;
    }

    @Transactional
    public User updateUser(User user) {
        logger.info("class:UserService , method: updateUser , message= Updating a user");
        User existingUser = userRepository.findByUserName(user.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        userRepository.persist(existingUser);
        return existingUser;
    }
    @Transactional
    public User deleteUser(User user) {
        logger.info("class:UserService , method: deleteUser , message= delete user");
        User existingUser = userRepository.findByUserName(user.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setActive(false);
        userRepository.persist(existingUser);
        return existingUser;
    }

}

