package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User create(User user);

    List<Beer> addBeerToWishList(int userId, int beerId);

    List<Beer> removeBeerFromWishList(int userId, int beerId);


}
