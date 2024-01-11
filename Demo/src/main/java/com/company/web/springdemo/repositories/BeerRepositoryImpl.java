package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.Style;
import com.company.web.springdemo.models.User;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@PropertySource("classpath:application.properties")
public class BeerRepositoryImpl implements BeerRepository {

    private final String dbUrl, dbUsername, dbPassword;
    private final StyleRepository styleRepository;
    private final UserRepository userRepository;

    public BeerRepositoryImpl(Environment environment, StyleRepository styleRepository, UserRepository userRepository) {
        this.dbUrl = environment.getProperty("database.url");
        this.dbUsername = environment.getProperty("database.username");
        this.dbPassword = environment.getProperty("database.password");

        this.styleRepository = styleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder) {
        String query = "select * from beers";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
        ) {
            List<Beer> result = getBeers(resultSet);
            return filterBeers(name, minAbv, maxAbv, styleId, sortBy, sortOrder, result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Beer get(int id) {
        String query = "SELECT * " +
                "from beers " +
                "where beer_id = ? ";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            //1 отговаря на ?(индекса който ще подаваме),
            // Тук броим от 1 за броя на ? за това по колко неща ще търсим
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Beer> result = getBeers(resultSet);
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("Beer", id);
                }
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Beer getByName(String name) {
        String query = "SELECT * " +
                "from beers " +
                "where name = ? ";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            //1 отговаря на ?(индекса който ще подаваме),
            // Тук броим от 1 за броя на ? за това по колко неща ще търсим
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Beer> result = getBeers(resultSet);
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("Beer", "name", name);
                }
                return result.get(0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Beer beer, User user) {
        String query = "INSERT INTO beers(name, abv, style_id, created_by) " +
                "values (?,?,?,?)";

        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            //1 отговаря на ?(индекса който ще подаваме),
            // Тук броим от 1,2,3,4 за броя на ? за това по колко неща ще търсим
            statement.setString(1, beer.getName());
            statement.setDouble(2, beer.getAbv());
            statement.setInt(3, beer.getStyle().getId());
            statement.setInt(4, beer.getCreatedBy().getId());

            int affectedRows = statement.executeUpdate();

//Solution from Telerik that will be improved
//            Beer newBeer = getByName(beer.getName());
//            beer.setId(newBeer.getId());

// Maybe not necessary
//
//            if (affectedRows == 0){
//                throw
//            }
            try (
                    ResultSet generatedKeys = statement.getGeneratedKeys()
            ) {
                while (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    beer.setId(userId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Beer beer) {
        String query = "UPDATE beers " +
                "set name =?, abv =?, style_id =? " +
                "where beer_id =?";

        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, beer.getName());
            statement.setDouble(2, beer.getAbv());
            statement.setInt(3, beer.getStyle().getId());
            statement.setInt(4, beer.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE from beers " +
                "WHERE beer_id= ?";
        try (
                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Beer> getBeers(ResultSet beerData) throws SQLException {
        List<Beer> beers = new ArrayList<>();
        while (beerData.next()) {
            Beer beer = new Beer();
            beer.setId(beerData.getInt("beer_id"));
            beer.setName(beerData.getString("name"));
            beer.setAbv(beerData.getDouble("abv"));
            Style style = styleRepository.get(beerData.getInt("style_id"));
            beer.setStyle(style);
            User user = userRepository.getById(beerData.getInt("created_by"));
            beer.setCreatedBy(user);
            beers.add(beer);
        }
        return beers;
    }

    private static List<Beer> filterBeers(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy,
                                          String sortOrder, List<Beer> result) {
        result = filterByName(result, name);
        result = filterByAbv(result, minAbv, maxAbv);
        result = filterByStyle(result, styleId);
        result = sortBy(result, sortBy);
        result = order(result, sortOrder);
        return result;
    }

    private static List<Beer> filterByName(List<Beer> beers, String name) {
        if (name != null && !name.isEmpty()) {
            beers = beers.stream()
                    .filter(beer -> containsIgnoreCase(beer.getName(), name))
                    .collect(Collectors.toList());
        }
        return beers;
    }

    private static List<Beer> filterByAbv(List<Beer> beers, Double minAbv, Double maxAbv) {
        if (minAbv != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getAbv() >= minAbv)
                    .collect(Collectors.toList());
        }

        if (maxAbv != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getAbv() <= maxAbv)
                    .collect(Collectors.toList());
        }

        return beers;
    }

    private static List<Beer> filterByStyle(List<Beer> beers, Integer styleId) {
        if (styleId != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getStyle().getId() == styleId)
                    .collect(Collectors.toList());
        }
        return beers;
    }

    private static List<Beer> sortBy(List<Beer> beers, String sortBy) {
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    beers.sort(Comparator.comparing(Beer::getName));
                    break;
                case "abv":
                    beers.sort(Comparator.comparing(Beer::getAbv));
                case "style":
                    beers.sort(Comparator.comparing(beer -> beer.getStyle().getName()));
                    break;
            }
        }
        return beers;
    }

    private static List<Beer> order(List<Beer> beers, String order) {
        if (order != null && !order.isEmpty()) {
            if (order.equals("desc")) {
                Collections.reverse(beers);
            }
        }
        return beers;
    }

    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
