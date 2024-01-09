package com.company.web.springdemo.helpers;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String INVALID_AUTHENTICATION_ERROR = "Invalid Credentials";
    public static final String INVALID_PASSWORD_ERROR = "InvalidPassword";
    public static final String INVALID_USERNAME_ERROR = "Invalid username";
    private final UserService service;

    @Autowired
    public AuthenticationHelper(UserService service) {
        this.service = service;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        // Telerik solution
        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = service.getByUsername(username);
            if (!user.getPassword().equals(password)){
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }


        //My Solution
//        String authorization = headers.getFirst(AUTHORIZATION_HEADER_NAME);
//        String[] credentials = authorization.split(":");
//        if (credentials.length == 2) {
//            String username = credentials[0];
//            String password = credentials[1];
//            try {
//                User user = service.getByUsername(username);
//                if (user != null && user.getPassword().equals(password)) {
//                    return user;
//                } else {
//                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_PASSWORD_ERROR);
//                }
//            } catch (EntityNotFoundException e) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_USERNAME_ERROR);
//            }
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication.");
//        }
    }



    //Telerik Solution
    private static String getUsername(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");
        if (firstSpaceIndex == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(0, firstSpaceIndex);
    }
    private static String getPassword(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");
        if (firstSpaceIndex == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpaceIndex + 1);
    }
}
