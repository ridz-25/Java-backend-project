package dao.impl;

import dao.UserDAO;
import model.Buyer;
import model.Seller;
import model.User;
import model.UserRole;
import util.DBConnection;
import util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users " +
                "(name, email, password, role, phone, created_at, active, " +
                "address, bio, skills, experience, is_verified) " +
                "VALUES (?, ?, ?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP), ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            // The service should hash the password before calling saveUser.
            ps.setString(3, user.getPassword());
            ps.setString(4, dbRole(user.getRole()));
            ps.setString(5, user.getPhone());
            ps.setTimestamp(6, user.getCreatedAt());
            ps.setBoolean(7, user.isActive());

            if (user instanceof Buyer) {
                Buyer buyer = (Buyer) user;
                ps.setString(8, buyer.getAddress());
                ps.setNull(9, java.sql.Types.VARCHAR);
                ps.setNull(10, java.sql.Types.VARCHAR);
                ps.setNull(11, java.sql.Types.VARCHAR);
                ps.setNull(12, java.sql.Types.BOOLEAN);
            } else if (user instanceof Seller) {
                Seller seller = (Seller) user;
                ps.setNull(8, java.sql.Types.VARCHAR);
                ps.setString(9, seller.getBio());
                ps.setString(10, seller.getSkills());
                ps.setString(11, seller.getExperience());
                ps.setBoolean(12, seller.isVerified());
            } else {
                throw new IllegalArgumentException("Unsupported user subtype");
            }
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not save user", e);
        }
    }

    @Override
    public Optional<User> getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find user by ID", e);
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find user by email", e);
        }
    }

    @Override
    public boolean validateLogin(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ? AND active = TRUE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && PasswordUtil.verifyPassword(password, rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not validate login", e);
        }
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name=?, email=?, phone=?, address=?, bio=?, " +
                "skills=?, experience=?, is_verified=? WHERE user_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            if (user instanceof Buyer) {
                Buyer buyer = (Buyer) user;
                ps.setString(4, buyer.getAddress());
                ps.setNull(5, java.sql.Types.VARCHAR);
                ps.setNull(6, java.sql.Types.VARCHAR);
                ps.setNull(7, java.sql.Types.VARCHAR);
                ps.setNull(8, java.sql.Types.BOOLEAN);
            } else if (user instanceof Seller) {
                Seller seller = (Seller) user;
                ps.setNull(4, java.sql.Types.VARCHAR);
                ps.setString(5, seller.getBio());
                ps.setString(6, seller.getSkills());
                ps.setString(7, seller.getExperience());
                ps.setBoolean(8, seller.isVerified());
            } else {
                throw new IllegalArgumentException("Unsupported user subtype");
            }
            ps.setInt(9, user.getUserId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not update user", e);
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        // Soft-delete/deactivate: preserves order and review history.
        String sql = "UPDATE users SET active = FALSE WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not deactivate user", e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String phone = rs.getString("phone");
        Timestamp createdAt = rs.getTimestamp("created_at");
        boolean active = rs.getBoolean("active");
        UserRole role = parseRole(rs.getString("role"));

        if (role == UserRole.BUYER) {
            return new Buyer(id, name, email, password, phone, createdAt,
                    active, rs.getString("address"));
        }
        return new Seller(id, name, email, password, phone, createdAt,
                active, rs.getString("bio"), rs.getString("skills"),
                rs.getString("experience"), rs.getBoolean("is_verified"));
    }

    // Supports either the current enum names ONE/TWO or readable DB values BUYER/SELLER.
    private String dbRole(UserRole role) {
        return role == UserRole.BUYER ? "BUYER" : "SELLER";
    }

    private UserRole parseRole(String value) {
        if ("BUYER".equalsIgnoreCase(value) || "ONE".equalsIgnoreCase(value)) return UserRole.BUYER;
        if ("SELLER".equalsIgnoreCase(value) || "TWO".equalsIgnoreCase(value)) return UserRole.SELLER;
        throw new IllegalArgumentException("Unknown user role in database: " + value);
    }
}
