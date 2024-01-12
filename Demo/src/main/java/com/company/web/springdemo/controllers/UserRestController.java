package com.company.web.springdemo.controllers;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        // try catch to get the exceptions from the UserService checks

        return userService.create(user);


    }
    @GetMapping("/{id}")
    public User getById(@PathVariable int id){
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
@GetMapping("/{id}/wish-list")
    public List<Beer> getUserWishList(@PathVariable int id){
        return new ArrayList<>(getById(id).getWishList());
    }


}
