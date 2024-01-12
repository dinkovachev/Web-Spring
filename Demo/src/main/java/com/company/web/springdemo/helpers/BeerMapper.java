package com.company.web.springdemo.helpers;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.BeerDto;
import com.company.web.springdemo.repositories.BeerRepository;
import com.company.web.springdemo.services.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeerMapper {

    private final BeerRepository beerRepository;
    private final StyleService styleService;

    @Autowired
    public BeerMapper(BeerRepository beerRepository, StyleService styleService) {

        this.beerRepository = beerRepository;
        this.styleService = styleService;

    }

    public Beer fromDto(int id, BeerDto dto) {
        Beer beer = beerRepository.get(id);
        return fromDto(dto, beer);
    }

    public Beer fromDto(BeerDto dto) {
        Beer beer = new Beer();
        beer.setName(dto.getName());
        beer.setAbv(dto.getAbv());
        beer.setStyle(styleService.get(dto.getStyleId()));
        return beer;
    }

    public Beer fromDto(BeerDto dto, Beer beer) {
        beer.setName(dto.getName());
        beer.setAbv(dto.getAbv());
        beer.setStyle(styleService.get(dto.getStyleId()));
        return beer;
    }
}
