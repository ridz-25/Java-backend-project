
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Order;
import src.model.OrderStatus;
import src.model.User;
import src.model.Buyer;
import src.model.Gig;
import src.model.GigStatus;

public class OrderService {

    // Temporary in-memory storage
    private ArrayList<Order> orders = new ArrayList<>();

    // Automatically generate Order IDs
    private int nextOrderId = 1;

    // Shared service objects
    private UserService userService;
    private GigService gigService;

    // Constructor injection
    public OrderService(
            UserService userService,
            GigService gigService) {

        this.userService = userService;
        this.gigService = gigService;
    }

    // Place new order
    public Order placeOrder(
            int buyerId,
            int gigId,
            String notes) {

        User user = userService.getUserById(buyerId);

        if (!(user instanceof Buyer) || !user.isActive()) {
            throw new IllegalArgumentException(
                "Only registered active buyers can place orders."
            );
        }

        Gig gig = gigService.getGigById(gigId);

        if (gig == null || gig.getStatus() != GigStatus.ACTIVE) {
            throw new IllegalArgumentException(
                "Gig is not available."
            );
        }

        if (gig.getSellerId() == buyerId) {
            throw new IllegalArgumentException(
                "Seller cannot order their own gig."
            );
        }

        Timestamp currentTime =
            new Timestamp(System.currentTimeMillis());

        Order order = new Order(
            nextOrderId++,
            buyerId,
            gigId,
            gig.getSellerId(),
            gig.getPrice(),
            OrderStatus.PENDING,
            currentTime,
            null,
            notes == null ? "" : notes.trim()
        );

        orders.add(order);

        return order;
    }

    // Find order by ID
    public Order getOrderById(int orderId) {

        for (Order order : orders) {

            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    // Get orders by Buyer
    public List<Order> getOrdersByBuyer(int buyerId) {

        ArrayList<Order> buyerOrders = new ArrayList<>();

        for (Order order : orders) {

            if (order.getBuyerId() == buyerId) {
                buyerOrders.add(order);
            }
        }

        return buyerOrders;
    }

    // Get orders by Seller
    public List<Order> getOrdersBySeller(int sellerId) {

        ArrayList<Order> sellerOrders = new ArrayList<>();

        for (Order order : orders) {

            if (order.getSellerId() == sellerId) {
                sellerOrders.add(order);
            }
        }

        return sellerOrders;
    }

    // Cancel order
    public boolean cancelOrder(int orderId, int buyerId) {

        Order order = getOrderById(orderId);

        if (order == null ||
            order.getBuyerId() != buyerId ||
            order.getStatus() == OrderStatus.COMPLETED ||
            order.getStatus() == OrderStatus.CANCELLED) {

            return false;
        }

        order.cancelOrder();

        return true;
    }

    // Update order status
    public boolean updateOrderStatus(
            int orderId,
            int sellerId,
            OrderStatus newStatus) {

        Order order = getOrderById(orderId);

        if (order == null ||
            order.getSellerId() != sellerId ||
            newStatus == null ||
            order.getStatus() == OrderStatus.CANCELLED ||
            order.getStatus() == OrderStatus.COMPLETED ||
            newStatus == OrderStatus.CANCELLED) {

            return false;
        }

        if (newStatus != OrderStatus.IN_PROGRESS &&
            newStatus != OrderStatus.COMPLETED) {

            return false;
        }

        if (order.getStatus() == OrderStatus.PENDING &&
            newStatus != OrderStatus.IN_PROGRESS) {

            return false;
        }

        order.updateStatus(newStatus);

        return true;
    }
}
