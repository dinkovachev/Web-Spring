package com.company.web.springdemo.services;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BeerService beerService;

    public UserServiceImpl(UserRepository userRepository, BeerService beerService) {
        this.userRepository = userRepository;
        this.beerService = beerService;
    }

    @Override
    public List<User> getAll() {

        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {

        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {

        return userRepository.getByUsername(username);
    }

    @Override
    public User create(User user) {
        //checkUsername -> Exception that will be thrown
        //checkPassword -> Exception that will be thrown
        //checkData -> Exception that will be thrown
        return userRepository.create(user);
    }

    @Override
    public List<Beer> addBeerToWishList(int userId, int beerId) {
        User user = userRepository.getById(userId);
        Beer beer = beerService.get(beerId);
        if (user.getWishList().add(beer)) {
            userRepository.update(user);
        }
        return new ArrayList<>(user.getWishList());
    }

    @Override
    public List<Beer> removeBeerFromWishList(int userId, int beerId) {
        User user = userRepository.getById(userId);
        Beer beer = beerService.get(beerId);
        if (user.getWishList().remove(beer)) {
            userRepository.update(user);
        }
        return new ArrayList<>(user.getWishList());
    }


}
