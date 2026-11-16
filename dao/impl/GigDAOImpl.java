package dao.impl;

import dao.GigDAO;
import model.Gig;
import model.GigStatus;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GigDAOImpl implements GigDAO {

    @Override
    public boolean saveGig(Gig gig) {
        String sql = "INSERT INTO gigs (title, description, seller_id, category_id, " +
                "price, delivery_time, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, gig.getTitle());
            ps.setString(2, gig.getDescription());
            ps.setInt(3, gig.getSellerId());
            ps.setInt(4, gig.getCategoryId());
            ps.setDouble(5, gig.getPrice());
            ps.setString(6, gig.getDeliveryTime());
            ps.setString(7, gig.getStatus().name());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not save gig", e);
        }
    }

    @Override
    public Optional<Gig> getGigById(int gigId) {
        String sql = "SELECT * FROM gigs WHERE gig_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gigId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapGig(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find gig", e);
        }
    }

    @Override
    public List<Gig> getAllGigs() {
        return queryGigs("SELECT * FROM gigs WHERE status <> 'DELETED' ORDER BY created_at DESC", null);
    }

    @Override
    public List<Gig> getGigsBySeller(int sellerId) {
        return queryGigs("SELECT * FROM gigs WHERE seller_id = ? ORDER BY created_at DESC", sellerId);
    }

    @Override
    public List<Gig> getGigsByCategory(int categoryId) {
        return queryGigs("SELECT * FROM gigs WHERE category_id = ? AND status = 'ACTIVE'", categoryId);
    }

    @Override
    public List<Gig> searchGigs(String keyword) {
        String sql = "SELECT * FROM gigs WHERE status = 'ACTIVE' " +
                "AND (title LIKE ? OR description LIKE ?) ORDER BY created_at DESC";
        List<Gig> gigs = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) gigs.add(mapGig(rs));
            }
            return gigs;
        } catch (SQLException e) {
            throw new RuntimeException("Could not search gigs", e);
        }
    }

    @Override
    public boolean updateGig(Gig gig) {
        String sql = "UPDATE gigs SET title=?, description=?, category_id=?, price=?, " +
                "delivery_time=?, status=?, updated_at=CURRENT_TIMESTAMP WHERE gig_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, gig.getTitle());
            ps.setString(2, gig.getDescription());
            ps.setInt(3, gig.getCategoryId());
            ps.setDouble(4, gig.getPrice());
            ps.setString(5, gig.getDeliveryTime());
            ps.setString(6, gig.getStatus().name());
            ps.setInt(7, gig.getGigId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not update gig", e);
        }
    }

    @Override
    public boolean deleteGig(int gigId) {
        String sql = "UPDATE gigs SET status='DELETED', updated_at=CURRENT_TIMESTAMP WHERE gig_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, gigId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete gig", e);
        }
    }

    private List<Gig> queryGigs(String sql, Integer parameter) {
        List<Gig> gigs = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (parameter != null) ps.setInt(1, parameter);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) gigs.add(mapGig(rs));
            }
            return gigs;
        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve gigs", e);
        }
    }

    private Gig mapGig(ResultSet rs) throws SQLException {
        return new Gig(
                rs.getInt("gig_id"), rs.getString("title"), rs.getString("description"),
                rs.getInt("seller_id"), rs.getInt("category_id"), rs.getDouble("price"),
                rs.getString("delivery_time"), GigStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at"), rs.getTimestamp("updated_at"),
                rs.getDouble("avg_rating"), rs.getInt("review_count")
        );
    }
}
