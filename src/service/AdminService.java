
package src.service;

import java.util.ArrayList;
import java.util.List;

import src.model.Admin;
import src.model.User;
import src.model.Gig;
import src.model.Order;
import src.model.OrderStatus;

public class AdminService {

    private Admin admin;
    private UserService userService;
    private GigService gigService;
    private OrderService orderService;

    // Constructor injection
    public AdminService(
            Admin admin,
            UserService userService,
            GigService gigService,
            OrderService orderService) {

        if (admin == null) {
            throw new IllegalArgumentException(
                "Valid admin is required."
            );
        }

        this.admin = admin;
        this.userService = userService;
        this.gigService = gigService;
        this.orderService = orderService;
    }

    // Check whether admin is active
    private boolean isAdminActive() {
        return admin.isActive();
    }

    // Get all registered users
    public List<User> getAllUsers() {

        if (!isAdminActive()) {
            return new ArrayList<>();
        }

        return userService.getAllUsers();
    }

    // Get user by ID
    public User getUserById(int userId) {

        if (!isAdminActive()) {
            return null;
        }

        return userService.getUserById(userId);
    }

    // Block a user
    public boolean blockUser(int userId) {

        if (!isAdminActive()) {
            return false;
        }

        User user = userService.getUserById(userId);

        if (user == null ||
            user.getRole() == admin.getRole() ||
            !user.isActive()) {
            return false;
        }

        user.setActive(false);
        return true;
    }

    // Unblock a user
    public boolean unblockUser(int userId) {

        if (!isAdminActive()) {
            return false;
        }

        User user = userService.getUserById(userId);

        if (user == null ||
            user.getRole() == admin.getRole() ||
            user.isActive()) {
            return false;
        }

        user.setActive(true);
        return true;
    }

    // Get all active gigs
    public List<Gig> getAllGigs() {

        if (!isAdminActive()) {
            return new ArrayList<>();
        }

        return gigService.getAllGigs();
    }

    // Get gig details
    public Gig getGigById(int gigId) {

        if (!isAdminActive()) {
            return null;
        }

        return gigService.getGigById(gigId);
    }

    // Get all orders
    public List<Order> getAllOrders() {

        if (!isAdminActive()) {
            return new ArrayList<>();
        }

        return orderService.getAllOrders();
    }

    // Get order details
    public Order getOrderById(int orderId) {

        if (!isAdminActive()) {
            return null;
        }

        return orderService.getOrderById(orderId);
    }

    // Platform report: total users
    public int getTotalUsers() {
        return getAllUsers().size();
    }

    // Platform report: active users
    public int getActiveUsers() {

        int count = 0;

        for (User user : getAllUsers()) {
            if (user.isActive()) {
                count++;
            }
        }

        return count;
    }

    // Platform report: blocked users
    public int getBlockedUsers() {

        int count = 0;

        for (User user : getAllUsers()) {
            if (!user.isActive()) {
                count++;
            }
        }

        return count;
    }

    // Platform report: active gigs
    public int getActiveGigCount() {
        return getAllGigs().size();
    }

    // Platform report: total orders
    public int getTotalOrders() {
        return getAllOrders().size();
    }

    // Platform report: completed orders
    public int getCompletedOrders() {

        int count = 0;

        for (Order order : getAllOrders()) {
            if (order.getStatus() == OrderStatus.COMPLETED) {
                count++;
            }
        }

        return count;
    }

    // Platform report: cancelled orders
    public int getCancelledOrders() {

        int count = 0;

        for (Order order : getAllOrders()) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                count++;
            }
        }

        return count;
    }

    // Total value of completed orders
    public double getCompletedOrderValue() {

        double total = 0.0;

        for (Order order : getAllOrders()) {
            if (order.getStatus() == OrderStatus.COMPLETED) {
                total += order.getAmount();
            }
        }

        return total;
    }
}
