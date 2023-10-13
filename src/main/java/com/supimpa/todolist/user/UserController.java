package com.supimpa.todolist.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.supimpa.todolist.exceptions.AlreadyExistsException;
import com.supimpa.todolist.exceptions.InvalidEntityException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserRepository userRepository;

    @PostMapping()
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> create(@RequestBody UserModel user) throws AlreadyExistsException, InvalidEntityException {
        if(this.userRepository.findByUsername(user.getUsername()) != null) {
            throw new AlreadyExistsException("Username already exists");
        }

        String password = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(password);

        try {
            this.userRepository.save(user);
        } catch(Exception e) {
            throw new InvalidEntityException("Invalid user entity");
        }

        return Map.of(
            "id", user.getId().toString(),
            "username", user.getUsername(),
            "name", user.getName(),
            "createdAt", user.getCreatedAt().toString()
        );
    }
}
