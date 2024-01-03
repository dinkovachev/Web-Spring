package com.telerikacademy.web.beertagprojects.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class Beer {
    @Positive(message = "Id should be positive.")
    private  int id;
    @NotNull(message = "Name can't be empty.")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 symbols.")
    private String name;
    @Positive(message = "ABV should be positive.")
    private double abv;

    public Beer() {
    }

    public Beer(int id, String name, double abv) {
        this.id = id;
        this.name = name;
        this.abv = abv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beer beer = (Beer) o;
        return id == beer.id && name == beer.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
