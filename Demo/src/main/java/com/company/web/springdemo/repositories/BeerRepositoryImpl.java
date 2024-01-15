package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
//JDBC implementation
//@PropertySource("classpath:application.properties")
public class BeerRepositoryImpl implements BeerRepository {

    //JDBC implementation
//    private final String dbUrl, dbUsername, dbPassword;
//    private final StyleRepository styleRepository;
//    private final UserRepository userRepository;
//@Autowired
//    public BeerRepositoryImpl(Environment environment, StyleRepository styleRepository, UserRepository userRepository) {
//        //JDBC implementation
////        this.dbUrl = environment.getProperty("database.url");
////        this.dbUsername = environment.getProperty("database.username");
////        this.dbPassword = environment.getProperty("database.password");
//
////        this.styleRepository = styleRepository;
////        this.userRepository = userRepository;
//    }

    //Hibernate Implementation
    private final SessionFactory sessionFactory;

    @Autowired
    public BeerRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Beer> get(FilterOptions filterOptions) {
        //Hibernate Implementation
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" from Beer ");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();

            filterOptions.getName().ifPresent(value -> {
                filters.add(" name like: beerName ");
                parameters.put("beerName", String.format("%%%s%%", value));
            });

            filterOptions.getMinAbv().ifPresent(value -> {
                filters.add(" abv >= :minAbv ");
                parameters.put("minAbv", value);
            });
            filterOptions.getMaxAbv().ifPresent(value -> {
                filters.add(" abv <= :maxAbv ");
                parameters.put("minAbv", value);
            });
            filterOptions.getStyleId().ifPresent(value -> {
                filters.add(" style.id = :styleId ");
                parameters.put("styleId", value);
            });


            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));
            Query<Beer> query = session.createQuery(queryString.toString(), Beer.class);
            query.setProperties(parameters);
            return query.list();
        }

    }

    private String generateOrderBy(FilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()){
            return "";
        }
        String orderBy = "";
        switch (filterOptions.getSortBy().get()){
            case "name":
                orderBy = "name";
                break;
            case "abv":
                orderBy = "abv";
                break;
            case "style":
                orderBy = "style.name";
                break;
        }
        orderBy = String.format(" order by %s", orderBy);
        if (filterOptions.getSortOrder().isPresent()
                && containsIgnoreCase(filterOptions.getSortOrder().get(), "desc")){
            orderBy = String.format(" %s desc ", orderBy);
        }
        return orderBy;
    }


    //
//
//        //JDBC implementation
////        String query = "select * from beers";
////        try (
////                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
////                Statement statement = connection.createStatement();
////                ResultSet resultSet = statement.executeQuery(query);
////        ) {
////            List<Beer> result = getBeers(resultSet);
////            return filterBeers(name, minAbv, maxAbv, styleId, sortBy, sortOrder, result);
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
//    }
//
@Override
public Beer get(int id) {
    //Hibernate Implementation
    try (
            Session session = sessionFactory.openSession()
    ) {
        Beer beer = session.get(Beer.class, id);
        if (beer == null) {
            throw new EntityNotFoundException("Beer", id);
        }
        return beer;
    }
}
//        //JDBC implementation
////        String query = "SELECT * " +
////                "from beers " +
////                "where beer_id = ? ";
////        try (
////                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
////                PreparedStatement statement = connection.prepareStatement(query)
////        ) {
////            //1 отговаря на ?(индекса който ще подаваме),
////            // Тук броим от 1 за броя на ? за това по колко неща ще търсим
////            statement.setInt(1, id);
////            try (ResultSet resultSet = statement.executeQuery()) {
////                List<Beer> result = getBeers(resultSet);
////                if (result.isEmpty()) {
////                    throw new EntityNotFoundException("Beer", id);
////                }
////                return result.get(0);
////            }
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
//
//    }
//
    @Override
    public Beer getByName(String name){
        //Hibernate Implementation
        try (Session session = sessionFactory.openSession()) {
            Query<Beer> query = session.createQuery("from Beer where name = :name", Beer.class);
            query.setParameter("name", name);
            List<Beer> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Beer", "name", name);
            }
            return result.get(0);
        }
    }
//        //JDBC implementation
////        String query = "SELECT * " +
////                "from beers " +
////                "where name = ? ";
////        try (
////                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
////                PreparedStatement statement = connection.prepareStatement(query)
////        ) {
////            //1 отговаря на ?(индекса който ще подаваме),
////            // Тук броим от 1 за броя на ? за това по колко неща ще търсим
////            statement.setString(1, name);
////            try (ResultSet resultSet = statement.executeQuery()) {
////                List<Beer> result = getBeers(resultSet);
////                if (result.isEmpty()) {
////                    throw new EntityNotFoundException("Beer", "name", name);
////                }
////                return result.get(0);
////            }
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
//    }
//
    @Override
    public void create(Beer beer, User user) {
        //Hibernate Implementation
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(beer);
            session.getTransaction().commit();
        }
    }
//        //JDBC implementation
////        String query = "INSERT INTO beers(name, abv, style_id, created_by) " +
////                "values (?,?,?,?)";
////
////        try (
////                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
////                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
////        ) {
////            //1 отговаря на ?(индекса който ще подаваме),
////            // Тук броим от 1,2,3,4 за броя на ? за това по колко неща ще търсим
////            statement.setString(1, beer.getName());
////            statement.setDouble(2, beer.getAbv());
////            statement.setInt(3, beer.getStyle().getId());
////            statement.setInt(4, beer.getCreatedBy().getId());
////
////            int affectedRows = statement.executeUpdate();
////
//////Solution from Telerik that will be improved
//////            Beer newBeer = getByName(beer.getName());
//////            beer.setId(newBeer.getId());
////
////// Maybe not necessary
//////
//////            if (affectedRows == 0){
//////                throw
//////            }
////            try (
////                    ResultSet generatedKeys = statement.getGeneratedKeys()
////            ) {
////                while (generatedKeys.next()) {
////                    int userId = generatedKeys.getInt(1);
////                    beer.setId(userId);
////                }
////            }
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
//
//    }
//
    @Override
    public void update(Beer beer){
            //Hibernate Implementation
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.merge(beer);
                session.getTransaction().commit();
                ;
            }
        }
//        //JDBC implementation
////        String query = "UPDATE beers " +
////                "set name =?, abv =?, style_id =? " +
////                "where beer_id =?";
////
////        try (
////                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
////                PreparedStatement statement = connection.prepareStatement(query);
////        ) {
////            statement.setString(1, beer.getName());
////            statement.setDouble(2, beer.getAbv());
////            statement.setInt(3, beer.getStyle().getId());
////            statement.setInt(4, beer.getId());
////
////            statement.executeUpdate();
////
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
//    }
//
    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(id);
            session.getTransaction().commit();
            ;
        }
    }
//        //JDBC implementation
////        String query = "DELETE from beers " +
////                "WHERE beer_id= ?";
////        try (
////                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
////                PreparedStatement preparedStatement = connection.prepareStatement(query);
////        ) {
////            preparedStatement.setInt(1, id);
////            preparedStatement.executeUpdate();
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
//
//    }
//
//    //JDBC implementation
////    private List<Beer> getBeers(ResultSet beerData) throws SQLException {
////        List<Beer> beers = new ArrayList<>();
////        while (beerData.next()) {
////            Beer beer = new Beer();
////            beer.setId(beerData.getInt("beer_id"));
////            beer.setName(beerData.getString("name"));
////            beer.setAbv(beerData.getDouble("abv"));
////            Style style = styleRepository.get(beerData.getInt("style_id"));
////            beer.setStyle(style);
////            User user = userRepository.getById(beerData.getInt("created_by"));
////            beer.setCreatedBy(user);
////            beers.add(beer);
////        }
////        return beers;
////    }
//
//    private static List<Beer> filterBeers(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy,
//                                          String sortOrder, List<Beer> result) {
//        result = filterByName(result, name);
//        result = filterByAbv(result, minAbv, maxAbv);
//        result = filterByStyle(result, styleId);
//        result = sortBy(result, sortBy);
//        result = order(result, sortOrder);
//        return result;
//    }
//
//    private static List<Beer> filterByName(List<Beer> beers, String name) {
//        if (name != null && !name.isEmpty()) {
//            beers = beers.stream()
//                    .filter(beer -> containsIgnoreCase(beer.getName(), name))
//                    .collect(Collectors.toList());
//        }
//        return beers;
//    }
//
//    private static List<Beer> filterByAbv(List<Beer> beers, Double minAbv, Double maxAbv) {
//        if (minAbv != null) {
//            beers = beers.stream()
//                    .filter(beer -> beer.getAbv() >= minAbv)
//                    .collect(Collectors.toList());
//        }
//
//        if (maxAbv != null) {
//            beers = beers.stream()
//                    .filter(beer -> beer.getAbv() <= maxAbv)
//                    .collect(Collectors.toList());
//        }
//
//        return beers;
//    }
//
//    private static List<Beer> filterByStyle(List<Beer> beers, Integer styleId) {
//        if (styleId != null) {
//            beers = beers.stream()
//                    .filter(beer -> beer.getStyle().getId() == styleId)
//                    .collect(Collectors.toList());
//        }
//        return beers;
//    }
//
//    private static List<Beer> sortBy(List<Beer> beers, String sortBy) {
//        if (sortBy != null && !sortBy.isEmpty()) {
//            switch (sortBy.toLowerCase()) {
//                case "name":
//                    beers.sort(Comparator.comparing(Beer::getName));
//                    break;
//                case "abv":
//                    beers.sort(Comparator.comparing(Beer::getAbv));
//                case "style":
//                    beers.sort(Comparator.comparing(beer -> beer.getStyle().getName()));
//                    break;
//            }
//        }
//        return beers;
//    }
//
//    private static List<Beer> order(List<Beer> beers, String order) {
//        if (order != null && !order.isEmpty()) {
//            if (order.equals("desc")) {
//                Collections.reverse(beers);
//            }
//        }
//        return beers;
//    }
//
    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
