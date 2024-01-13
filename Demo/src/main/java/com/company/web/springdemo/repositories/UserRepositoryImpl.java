package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
public class UserRepositoryImpl implements UserRepository {

    //JDBC implementation
//    private final String dbUrl, dbUsername, dbPassword;
//
//    public UserRepositoryImpl(Environment environment) {
//        this.dbUrl = environment.getProperty("database.url");
//        this.dbUsername = environment.getProperty("database.username");
//        this.dbPassword = environment.getProperty("database.password");
//    }

    //Hibernate Implementation
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        //Hibernate Implementation
        try(Session session = sessionFactory.openSession()){
            return session.createQuery("from User", User.class).list();
        }
        //JDBC implementation
//        String query = "SELECT * FROM users ";
//        try (
//                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery(query);
//        ) {
//            return getUsers(resultSet);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public User getById(int id) {
        //Hibernate Implementation
        try(Session session = sessionFactory.openSession()) {
            User user = session.get(User.class,id);
            if (user == null){
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
        //JDBC implementation
//        String query = "SELECT * " +
//                "from users " +
//                "where user_id = ? ";
//        try (
//                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//                PreparedStatement statement = connection.prepareStatement(query)
//        ) {
//            //1 отговаря на ?(индекса който ще подаваме),
//            // Тук броим от 1 за броя на ? за това по колко неща ще търсим
//            statement.setInt(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                List<User> result = getUsers(resultSet);
//                if (result.isEmpty()) {
//                    throw new EntityNotFoundException("User", id);
//                }
//                return result.get(0);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public User getByUsername(String username) {
        //Hibernate Implementation
        try(Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()){
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
        //JDBC implementation
//        String query = "SELECT * " +
//                "from users " +
//                "where username = ? ";
//        try (
//                Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
//                PreparedStatement statement = connection.prepareStatement(query)
//        ) {
//            //1 отговаря на ?(индекса който ще подаваме),
//            // Тук броим от 1 за броя на ? за това по колко неща ще търсим
//            statement.setString(1, username);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                List<User> result = getUsers(resultSet);
//                if (result.isEmpty()) {
//                    throw new EntityNotFoundException("User", "username", username);
//                }
//                return result.get(0);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public User create(User user) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
        return user;


    }

    @Override
    public void update(User user) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }
    //JDBC implementation
//    private List<User> getUsers(ResultSet userData) throws SQLException {
//        List<User> users = new ArrayList<>();
//        while (userData.next()) {
//            User user = new User();
//            user.setId(userData.getInt("user_id"));
//            user.setUsername(userData.getString("username"));
//            user.setPassword(userData.getString("password"));
//            user.setFirstName(userData.getString("first_name"));
//            user.setLastName(userData.getString("last_name"));
//            user.setEmail(userData.getString("email"));
//            user.setAdmin(userData.getBoolean("is_admin"));
//            users.add(user);
//        }
//        return users;
//    }
}
