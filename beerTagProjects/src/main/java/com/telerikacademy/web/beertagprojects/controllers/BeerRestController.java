package com.telerikacademy.web.beertagprojects.controllers;

import com.telerikacademy.web.beertagprojects.configuration.BeanConfiguration;
import com.telerikacademy.web.beertagprojects.exceptions.DuplicateEntityException;
import com.telerikacademy.web.beertagprojects.exceptions.EntityNotFoundException;
import com.telerikacademy.web.beertagprojects.models.Beer;
import com.telerikacademy.web.beertagprojects.services.BeerService;
import com.telerikacademy.web.beertagprojects.services.BeerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private BeerService service;

    public BeerRestController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
        this.service = context.getBean(BeerService.class);
    }

    @GetMapping
    public List<Beer> getBeers() {
        return service.getBeers();
    }

    @GetMapping("/{id}")
    public Beer getById(@PathVariable int id) {
        return findBeerById(id);
    }

    private Beer findBeerById(int id) {
        try {
            return service.findBeerById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Beer create(@Valid @RequestBody Beer beer) {
        try {
            service.create(beer);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return beer;
    }

    @PutMapping("/{id}")
    public Beer update(@PathVariable int id, @Valid @RequestBody Beer newBeer) {
        try {
            service.update(newBeer);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return newBeer;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            service.delete(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
