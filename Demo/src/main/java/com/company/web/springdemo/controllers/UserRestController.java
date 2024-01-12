package com.company.web.springdemo.controllers;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.helpers.AuthenticationHelper;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserRestController {

    public static final String YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR = "You are not authorized to browse user information.";
    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR);
            }
            return userService.getAll();
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR);
        }

    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            tryAuthorize(id, headers);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR);
        }
    }


    @GetMapping("/{id}/wish-list")
    public List<Beer> getUserWishList(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = tryAuthorize(id, headers);
            return new ArrayList<>(user.getWishList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR);
        }
    }

    @PutMapping("/{userId}/wish-list/{beerId}")
    public List<Beer> addBeerToWishList(@PathVariable int userId,
                                        @PathVariable int beerId,
                                        @RequestHeader HttpHeaders headers) {
        try {
            tryAuthorize(userId, headers);
            return userService.addBeerToWishList(userId, beerId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR);
        }
    }

   @DeleteMapping("/{userId}/wish-list/{beerId}")
        public List<Beer> removeBeerFromWishList(@PathVariable int userId,
                                                 @PathVariable int beerId,
                                                 @RequestHeader HttpHeaders headers){
       try {
           tryAuthorize(userId, headers);
           return userService.removeBeerFromWishList(userId, beerId);
       } catch (EntityNotFoundException e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
       } catch (AuthorizationException e) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                   YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR);
       }
    }

    @PostMapping
    public User create(@RequestBody User user) {
        // try catch to get the exceptions from the UserService checks

        return userService.create(user);


    }


    private User tryAuthorize(int id, HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        if (!user.isAdmin() && user.getId() != id) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION_ERROR);
        }
        return user;
    }


}
