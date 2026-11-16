package dao.impl;

import dao.CategoryDAO;
import model.Category;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean saveCategory(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not save category", e);
        }
    }

    @Override
    public Optional<Category> getCategoryById(int categoryId) {
        String sql = "SELECT * FROM categories WHERE category_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapCategory(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find category", e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM categories ORDER BY name";
        List<Category> categories = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) categories.add(mapCategory(rs));
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve categories", e);
        }
    }

    @Override
    public boolean updateCategory(Category category) {
        String sql = "UPDATE categories SET name=?, description=? WHERE category_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not update category", e);
        }
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE category_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete category", e);
        }
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        return new Category(rs.getInt("category_id"),
                rs.getString("name"), rs.getString("description"));
    }
}

