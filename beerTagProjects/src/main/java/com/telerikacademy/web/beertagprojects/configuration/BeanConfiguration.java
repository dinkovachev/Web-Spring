package com.telerikacademy.web.beertagprojects.configuration;

import com.telerikacademy.web.beertagprojects.repositories.BeerMapRepositoryImpl;
import com.telerikacademy.web.beertagprojects.repositories.BeerRepository;
import com.telerikacademy.web.beertagprojects.repositories.BeerRepositoryImpl;
import com.telerikacademy.web.beertagprojects.services.BeerService;
import com.telerikacademy.web.beertagprojects.services.BeerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public BeerService beerService() {
        return new BeerServiceImpl(beerRepository());
    }

    //    @Bean
//    public BeerRepository beerRepository(){
//        return new BeerRepositoryImpl();
//    }
    @Bean
    public BeerRepository beerRepository() {
        return new BeerMapRepositoryImpl();
    }
}
