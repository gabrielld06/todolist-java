package com.supimpa.todolist.user;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping()
    public void create(@RequestBody UserModel user) {
        Logger logger = Logger.getLogger(UserController.class.getName());
        logger.info(user.getName());
        logger.info(user.getEmail());
        logger.info(user.getPassword());
    }
}
