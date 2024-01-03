package com.telerikacademy.web.beertagprojects.controllers;

import com.telerikacademy.web.beertagprojects.models.Beer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {
    private final List<Beer> beers;

    public BeerRestController() {
        this.beers = new ArrayList<>();

        beers.add(new Beer(1, "Corona", 4.5));
        beers.add(new Beer(2, "HoeGarden", 5.5));
    }

    @GetMapping
    public List<Beer> getBeers() {
        return beers;
    }

    @GetMapping("/{id}")
    public Beer getById(@PathVariable int id) {
        return findBeerById(id);
    }

    private Beer findBeerById(int id) {
        return beers.stream()
                .filter(beer -> beer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Beer with id %d not found.", id)));
    }

    @PostMapping
    public Beer create(@Valid @RequestBody Beer beer) {
        beers.add(beer);
        return beer;
    }

    @PutMapping("/{id}")
    public Beer update(@PathVariable int id, @Valid @RequestBody Beer newBeer) {
        Beer beer = findBeerById(id);
        beer.setName(newBeer.getName());
        beer.setAbv(newBeer.getAbv());
        return beer;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        Beer beer = findBeerById(id);
        beers.remove(beer);
    }

}
