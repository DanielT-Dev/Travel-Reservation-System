package service;

import model.User;
import repository.UserDAO;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // CREATE
    public void addUser(String name, String email, String password) {
        User user = new User(name, email, password);
        userDAO.save(user);
    }

    // READ ALL
    public List<User> findAll() {
        return userDAO.findAll();
    }

    // READ BY ID
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userDAO.findById(id));
    }

    // UPDATE
    public void updateUser(int id, String name, String email, String password) {
        User user = new User(id, name, email, password);
        userDAO.update(user);
    }

    // DELETE
    public void deleteUser(int id) {
        userDAO.deleteById(id);
    }

    // LOGIN (important for your controller)
    public Optional<User> login(String email, String password) {
        return userDAO.findAll().stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst();
    }

    // PAGINATION
    public List<User> getUsersPage(int page) {
        return userDAO.findPage(page);
    }

    public int getTotalUsers() {
        return userDAO.getTotalUsers();
    }
}