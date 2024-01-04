package com.telerikacademy.web.beertagprojects.services;

import com.telerikacademy.web.beertagprojects.exceptions.DuplicateEntityException;
import com.telerikacademy.web.beertagprojects.exceptions.EntityNotFoundException;
import com.telerikacademy.web.beertagprojects.models.Beer;
import com.telerikacademy.web.beertagprojects.repositories.BeerRepository;
import com.telerikacademy.web.beertagprojects.repositories.BeerRepositoryImpl;

import java.util.List;

public class BeerServiceImpl implements BeerService {

    private BeerRepository repository;
    public BeerServiceImpl(BeerRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Beer> getBeers(){
        return repository.getBeers();
    }

    @Override
    public Beer findBeerById(int id){
        return repository.findBeerById(id);
    }
    @Override
    public void create(Beer beer){
        boolean duplicateExists = true;
        try {
            repository.getByName(beer.getName());
        } catch (EntityNotFoundException e){
            duplicateExists = false;
        }
        if (duplicateExists){
            throw new DuplicateEntityException("Beer", "name", beer.getName());
        }

        repository.create(beer);
    }
    @Override
    public void update(Beer beer){
        boolean duplicateExists = true;
        try {
            Beer existingBeer = repository.getByName(beer.getName());
            if (existingBeer.getId() == beer.getId()){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e){
            duplicateExists = false;
        }
        if (duplicateExists){
            throw new DuplicateEntityException("Beer", "name", beer.getName());
        }
        repository.update(beer);
    }

    @Override
    public void delete(int id){
        repository.delete(id);
    }
}
