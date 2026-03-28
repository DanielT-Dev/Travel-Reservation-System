package service;

import model.User;
import repository.UserDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void createUser(User user) {
        userDAO.add(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userDAO.getAll());
    }

    public User getUserById(int id) {
        return userDAO.findById(id);
    }

    public void updateUser(User user) {
        userDAO.update(user, user.getId());
    }

    public void deleteUser(int id) {
        User user = userDAO.findById(id);
        if (user != null) {
            userDAO.delete(user);
        }
    }

    public List<User> getUserPage(int pageNumber) {
        return userDAO.findPage(pageNumber);
    }

    public int getTotalUsers() {
        return userDAO.getTotalUsers();
    }

    public Optional<User> login(String email, String password) {
        return userDAO.getAll().stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst();
    }
}