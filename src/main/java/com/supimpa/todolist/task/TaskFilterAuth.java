package com.supimpa.todolist.task;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.supimpa.todolist.user.IUserRepository;
import com.supimpa.todolist.user.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TaskFilterAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Only for tasks
        if(!request.getServletPath().startsWith("/task")) {
            filterChain.doFilter(request, response);
            return;
        }

        final int basicLenght = "Basic".length();
        final String authErrorMessage = "Erro de autenticação";

        // Get credentials
        String authorization = request.getHeader("Authorization");

        if(authorization == null || authorization.length() < basicLenght) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authErrorMessage);
            return;
        }

        String authCode = authorization.substring(basicLenght).trim();
        String[] credentials = new String(Base64.getDecoder().decode(authCode)).split(":");

        if(credentials.length != 2) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authErrorMessage);
            return;
        }

        String username = credentials[0];
        String password = credentials[1];

        // Verify user
        UserModel user = this.userRepository.findByUsername(username);

        if(user == null || !BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), authErrorMessage);
            return;
        }

        request.setAttribute("userId", user.getId());

        filterChain.doFilter(request, response);
    }

    
}
