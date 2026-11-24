package src.model;

import java.sql.Timestamp;
import java.util.List;

public class Admin extends User {

    public Admin(int userId, String name, String email,
        String password, String phone, Timestamp createdAt,
        boolean active) {

        super(userId, name, email, password,
            UserRole.ADMIN, phone, createdAt, active);
    }

    public void manageUsers() {
        System.out.println("Managing users...");
    }

    public void manageGigs() {
        System.out.println("Managing gigs...");
    }

    public void manageOrders() {
        System.out.println("Managing orders...");
    }

    public void blockUser(int userId) {
        System.out.println("User " + userId + " has been blocked.");
    }

    public void viewReports() {
        System.out.println("Viewing platform reports...");
    }

    @Override
    public void displayDashboard() {
        System.out.println("Welcome to the Admin Dashboard");
    }
}