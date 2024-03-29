package com.company.web.springdemo.repositories;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;

import java.util.List;

public interface BeerRepository {

    List<Beer> get(FilterOptions filterOptions);

    Beer get(int id);

    Beer getByName(String name);

    void create(Beer beer, User user);

    void update(Beer beer);

    void delete(int id);

}
