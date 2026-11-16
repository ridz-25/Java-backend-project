package dao;

import model.Order;
import model.OrderStatus;
import java.util.List;
import java.util.Optional;

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
