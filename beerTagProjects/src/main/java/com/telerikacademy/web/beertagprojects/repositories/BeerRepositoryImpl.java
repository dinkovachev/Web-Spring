package com.telerikacademy.web.beertagprojects.repositories;

import com.telerikacademy.web.beertagprojects.exceptions.EntityNotFoundException;
import com.telerikacademy.web.beertagprojects.models.Beer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BeerRepositoryImpl implements BeerRepository {
    private List<Beer> beers;

    public BeerRepositoryImpl() {
        this.beers = new ArrayList<>();

        beers.add(new Beer(1, "Corona", 4.5));
        beers.add(new Beer(2, "HoeGarden", 5.5));
    }

    @Override
    public List<Beer> getBeers() {
        return beers;
    }

    @Override
    public Beer findBeerById(int id) {
        return beers.stream()
                .filter(beer -> beer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Beer", id));
    }

    @Override
    public Beer getByName(String name) {
        return beers.stream()
                .filter(beer -> beer.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Beer", "name", name));
    }

    @Override
    public void create(Beer beer) {
        beers.add(beer);
    }

    @Override
    public void update(Beer beer) {
        Beer beerToUpdate = findBeerById(beer.getId());
        beerToUpdate.setName(beer.getName());
        beerToUpdate.setAbv(beer.getAbv());
    }

    @Override
    public void delete(int id) {
        Beer beerToDelete = findBeerById(id);
        beers.remove(beerToDelete);
    }
}
