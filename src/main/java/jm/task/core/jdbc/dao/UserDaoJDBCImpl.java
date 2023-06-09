package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private List <User> users = null;
    private static final String sqlCommandCreateUserTable =
            "CREATE TABLE IF NOT EXISTS users (Id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), lastName VARCHAR(100), age INT(3))";
    private static final String sqlCommandSaveUser =
            "INSERT INTO users (name, lastName, age) VALUES( ?, ?, ?)";
    private static final String sqlCommandDeleteUser =
            "DELETE FROM users WHERE Id=?";
    private static final String sqlCommandDeleteUserTable =
            "DROP TABLE IF EXISTS users";
    private static final String sqlCommandClearTable =
            "TRUNCATE TABLE users";
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCommandCreateUserTable);
            System.out.println("Таблица создана!");
            connection.close();
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Таблица уже существует");
        } catch (SQLException e) {
            System.out.println("Таблица не создана!");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCommandDeleteUserTable);
            System.out.println("Таблица удалена!");
            connection.close();
        } catch (SQLSyntaxErrorException e) {
            System.out.println("Таблицы не существует!");
        } catch (SQLException e) {
            System.out.println("Таблица не удалена!");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = Util.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sqlCommandSaveUser)) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.execute();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Пользователь не добавлен!");
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        Connection connection = Util.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sqlCommandDeleteUser)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            System.out.println("User по идексу " + id + " удален из базы данных");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Ошибка удаления!");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        Connection connection = Util.getConnection();
        try  (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT * FROM users");
            users = new ArrayList<>();
            while (result.next()) {
                User user = new User(result.getString("name"), result.getString("lastName"), result.getByte("age"));
                user.setId(result.getLong("Id"));
                users.add(user);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlCommandClearTable);
            System.out.println("Таблица очищена!");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Таблица не очищена!");
            e.printStackTrace();
        }
    }
}
