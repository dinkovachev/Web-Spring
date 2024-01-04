package com.telerikacademy.web.beertagprojects.repositories;

import com.telerikacademy.web.beertagprojects.exceptions.EntityNotFoundException;
import com.telerikacademy.web.beertagprojects.models.Beer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BeerMapRepositoryImpl implements BeerRepository {

    private Map<Integer, Beer> beeers;

    public BeerMapRepositoryImpl() {
        this.beeers = new HashMap<>();
    }

    @Override
    public List<Beer> getBeers() {
        return new ArrayList<>(beeers.values());
    }

    @Override
    public Beer findBeerById(int id) {
        Beer beer = beeers.get(id);
        if (beer == null) {
            throw new EntityNotFoundException("Beer", id);
        }
        return beer;
    }

    @Override
    public Beer getByName(String name) {
        return beeers
                .values()
                .stream()
                .filter(beer -> beer.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Beer", "name", name));
    }

    @Override
    public void create(Beer beer) {
        beeers.put(beer.getId(), beer);
    }

    @Override
    public void update(Beer beer) {
        beeers.replace(beer.getId(), beer);
    }

    @Override
    public void delete(int id) {
        beeers.remove(id);
    }
}
