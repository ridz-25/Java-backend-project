package dao;

import model.User;
import java.util.Optional;

public interface UserDAO {

    boolean saveUser(User user);

    Optional<User> getUserById(int userId);

    Optional<User> getUserByEmail(String email);

    boolean validateLogin(String email, String password);

    boolean updateUser(User user);

    boolean deleteUser(int userId);
}