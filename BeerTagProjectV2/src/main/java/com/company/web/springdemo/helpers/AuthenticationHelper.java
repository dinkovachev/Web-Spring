package com.company.web.springdemo.helpers;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";

    private UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.getByUsername(username);
            if (user.getPassword().equals(password)){
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e){
            throw new AuthorizationException(e.getMessage());
        }

    }

    private String getUsername(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");
        if (firstSpaceIndex == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(0, firstSpaceIndex);
    }

    private String getPassword(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");
        if (firstSpaceIndex == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpaceIndex + 1);

    }
}


