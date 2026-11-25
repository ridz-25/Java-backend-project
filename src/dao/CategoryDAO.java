package src.dao;

import java.util.List;
import java.util.Optional;

import src.model.Category;

public interface CategoryDAO {

    boolean saveCategory(Category category);

    Optional<Category> getCategoryById(int categoryId);

    List<Category> getAllCategories();

    boolean updateCategory(Category category);

    boolean deleteCategory(int categoryId);
}
