package com.telerikacademy.web.beertagprojects.services;

import com.telerikacademy.web.beertagprojects.models.Beer;

import java.util.List;

public interface BeerService {
    List<Beer> getBeers();

    Beer findBeerById(int id);

    void create(Beer beer);

    void update(Beer beer);

    void delete(int id);
}
