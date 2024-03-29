package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Style;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class StyleRepositoryImpl implements StyleRepository {
    //JDBC implementation
   // private final String dbUrl, dbUsername, dbPassword;
    //
//    public StyleRepositoryImpl(Environment environment) {
//        this.dbUrl = environment.getProperty("database.url");
//        this.dbUsername = environment.getProperty("database.username");
//        this.dbPassword = environment.getProperty("database.password");
//    }
    private final SessionFactory sessionFactory;

    public StyleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Style> get() {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Style", Style.class).list();
        }
        //JDBC implementation
//        String query = "SELECT * from styles ";
//        try (
//                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery(query);
//        ) {
//            //Need to double check if it is ok
//            List<Style> result = getStyles(resultSet);
//            return result;
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

    }

    @Override
    public Style get(int id) {
        try(Session session = sessionFactory.openSession()) {
            Style style = session.get(Style.class,id);
            if (style == null){
                throw new EntityNotFoundException("Style", id);
            }
            return style;
        }
        //JDBC implementation
//        String query = "SELECT * " +
//                "from styles " +
//                "where style_id = ? ";
//        try (
//                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//                PreparedStatement statement = connection.prepareStatement(query)
//        ) {
//            //1 отговаря на ?(индекса който ще подаваме),
//            // Тук броим от 1 за броя на ? за това по колко неща ще търсим
//            statement.setInt(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                List<Style> result = getStyles(resultSet);
//                if (result.isEmpty()) {
//                    throw new EntityNotFoundException("Style", id);
//                }
//                return result.get(0);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }
    //JDBC implementation
//    private List<Style> getStyles(ResultSet stylesData) throws SQLException {
//        List<Style> styles = new ArrayList<>();
//        while (stylesData.next()) {
//            Style style = new Style();
//            style.setId(stylesData.getInt("style_id"));
//            style.setName(stylesData.getString("name"));
//            styles.add(style);
//        }
//        return styles;
//    }
}
