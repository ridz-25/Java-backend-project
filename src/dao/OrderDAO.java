package src.dao;

import java.util.List;
import java.util.Optional;

import src.model.Order;
import src.model.OrderStatus;

public interface OrderDAO {

    boolean saveOrder(Order order);

    Optional<Order> getOrderById(int orderId);

    List<Order> getOrdersByBuyer(int buyerId);

    List<Order> getOrdersBySeller(int sellerId);

    boolean updateOrderStatus(
        int orderId,
        OrderStatus status
    );

    boolean updateOrder(Order order);

    boolean deleteOrder(int orderId);
}
