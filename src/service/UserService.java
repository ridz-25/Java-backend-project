
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.User;
import src.model.Buyer;
import src.model.Seller;
import src.util.ValidationUtil;
import src.util.PasswordUtil;

public class UserService {

    // Temporary in-memory storage
    private ArrayList<User> users = new ArrayList<>();

    // Automatically generate user IDs
    private int nextUserId = 1;

    // Check whether email already exists
    private boolean isEmailRegistered(String email) {

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }

        return false;
    }

    // Register Buyer
    public Buyer registerBuyer(
            String name,
            String email,
            String password,
            String phone,
            String address) {

        if (ValidationUtil.isEmpty(name) ||
            ValidationUtil.isEmpty(address)) {
            throw new IllegalArgumentException(
                "Name and address cannot be empty."
            );
        }

        validateCommonDetails(email, password, phone);

        if (isEmailRegistered(email)) {
            throw new IllegalArgumentException(
                "Email is already registered."
            );
        }

        String hashedPassword =
            PasswordUtil.hashPassword(password);

        Buyer buyer = new Buyer(
            nextUserId++,
            name.trim(),
            email.trim(),
            hashedPassword,
            phone.trim(),
            new Timestamp(System.currentTimeMillis()),
            true,
            address.trim()
        );

        users.add(buyer);

        return buyer;
    }

    // Register Seller
    public Seller registerSeller(
            String name,
            String email,
            String password,
            String phone,
            String bio,
            String skills,
            String experience) {

        if (ValidationUtil.isEmpty(name)) {
            throw new IllegalArgumentException(
                "Name cannot be empty."
            );
        }

        validateCommonDetails(email, password, phone);

        if (isEmailRegistered(email)) {
            throw new IllegalArgumentException(
                "Email is already registered."
            );
        }

        String hashedPassword =
            PasswordUtil.hashPassword(password);

        Seller seller = new Seller(
            nextUserId++,
            name.trim(),
            email.trim(),
            hashedPassword,
            phone.trim(),
            new Timestamp(System.currentTimeMillis()),
            true,
            bio == null ? "" : bio.trim(),
            skills == null ? "" : skills.trim(),
            experience == null ? "" : experience.trim(),
            false
        );

        users.add(seller);

        return seller;
    }

    // Common validation
    private void validateCommonDetails(
            String email,
            String password,
            String phone) {

        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException(
                "Invalid email address."
            );
        }

        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException(
                "Password must contain at least 8 characters, "
                + "uppercase, lowercase, digit and special character."
            );
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException(
                "Invalid phone number."
            );
        }
    }

    // Login
    public User login(String email, String password) {

        if (ValidationUtil.isEmpty(email) ||
            ValidationUtil.isEmpty(password)) {
            return null;
        }

        for (User user : users) {

            if (user.getEmail().equalsIgnoreCase(email.trim())
                && user.isActive()
                && PasswordUtil.verifyPassword(
                    password, user.getPassword())) {

                return user;
            }
        }

        return null;
    }

    // Find user using ID
    public User getUserById(int userId) {

        for (User user : users) {

            if (user.getUserId() == userId) {
                return user;
            }
        }

        return null;
    }

    // Get all registered users
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Update common profile details
    public boolean updateProfile(
            int userId,
            String name,
            String phone) {

        User user = getUserById(userId);

        if (user == null ||
            ValidationUtil.isEmpty(name) ||
            !ValidationUtil.isValidPhone(phone)) {
            return false;
        }

        user.setName(name.trim());
        user.setPhone(phone.trim());

        return true;
    }
}
