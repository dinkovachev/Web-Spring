package com.telerikacademy.web.beertagprojects.repositories;

import com.telerikacademy.web.beertagprojects.models.Beer;

import java.util.List;

public interface BeerRepository {
    List<Beer> getBeers();

    Beer findBeerById(int id);

    Beer getByName(String name);

    void create(Beer beer);

    void update(Beer beer);

    void delete(int id);
}
