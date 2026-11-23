package src.dao.impl;

import src.dao.OrderDAO;
import src.model.Order;
import src.model.OrderStatus;
import src.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean saveOrder(Order order) {
        String sql = "INSERT INTO orders (buyer_id, gig_id, seller_id, amount, status, " +
                "order_date, delivery_date, notes) VALUES (?, ?, ?, ?, ?, " +
                "COALESCE(?, CURRENT_TIMESTAMP), ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getBuyerId());
            ps.setInt(2, order.getGigId());
            ps.setInt(3, order.getSellerId());
            ps.setDouble(4, order.getAmount());
            ps.setString(5, order.getStatus().name());
            ps.setTimestamp(6, order.getOrderDate());
            ps.setTimestamp(7, order.getDeliveryDate());
            ps.setString(8, order.getNotes());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not save order", e);
        }
    }

    @Override
    public Optional<Order> getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapOrder(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find order", e);
        }
    }

    @Override
    public List<Order> getOrdersByBuyer(int buyerId) {
        return queryOrders("SELECT * FROM orders WHERE buyer_id=? ORDER BY order_date DESC", buyerId);
    }

    @Override
    public List<Order> getOrdersBySeller(int sellerId) {
        return queryOrders("SELECT * FROM orders WHERE seller_id=? ORDER BY order_date DESC", sellerId);
    }

    @Override
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status=? WHERE order_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, orderId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not update order status", e);
        }
    }

    @Override
    public boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET amount=?, status=?, delivery_date=?, notes=? WHERE order_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, order.getAmount());
            ps.setString(2, order.getStatus().name());
            ps.setTimestamp(3, order.getDeliveryDate());
            ps.setString(4, order.getNotes());
            ps.setInt(5, order.getOrderId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not update order", e);
        }
    }

    @Override
    public boolean deleteOrder(int orderId) {
        // Prefer cancellation/status update over physical deletion in a real marketplace.
        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    private List<Order> queryOrders(String sql, int id) {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) orders.add(mapOrder(rs));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve orders", e);
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("order_id"), rs.getInt("buyer_id"), rs.getInt("gig_id"),
                rs.getInt("seller_id"), rs.getDouble("amount"),
                OrderStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("order_date"), rs.getTimestamp("delivery_date"),
                rs.getString("notes")
        );
    }
}

