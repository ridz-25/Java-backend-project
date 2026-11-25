
package src.service;

import java.util.ArrayList;
import java.util.List;

import src.model.Category;
import src.util.ValidationUtil;

public class CategoryService {

    // Temporary in-memory storage
    private ArrayList<Category> categories = new ArrayList<>();

    // Automatically generate category IDs
    private int nextCategoryId = 1;

    // Check duplicate category name
    private boolean isDuplicateCategory(String name) {

        for (Category category : categories) {

            if (category.getName().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }

        return false;
    }

    // Add new category
    public Category addCategory(String name, String description) {

        if (ValidationUtil.isEmpty(name)) {
            throw new IllegalArgumentException(
                "Category name cannot be empty."
            );
        }

        if (ValidationUtil.isEmpty(description)) {
            throw new IllegalArgumentException(
                "Category description cannot be empty."
            );
        }

        if (isDuplicateCategory(name)) {
            throw new IllegalArgumentException(
                "Category already exists."
            );
        }

        Category category = new Category(
            nextCategoryId++,
            name.trim(),
            description.trim()
        );

        categories.add(category);

        return category;
    }

    // Find category using ID
    public Category getCategoryById(int categoryId) {

        for (Category category : categories) {

            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }

        return null;
    }

    // Get all categories
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    // Update category
    public boolean updateCategory(
            int categoryId,
            String name,
            String description) {

        Category category = getCategoryById(categoryId);

        if (category == null ||
            ValidationUtil.isEmpty(name) ||
            ValidationUtil.isEmpty(description)) {
            return false;
        }

        // Allow keeping the same name while updating description.
        for (Category existing : categories) {

            if (existing.getCategoryId() != categoryId &&
                existing.getName().equalsIgnoreCase(name.trim())) {

                return false;
            }
        }

        category.updateCategoryName(
            name.trim(),
            description.trim()
        );

        return true;
    }

    // Delete category
    public boolean deleteCategory(int categoryId) {

        Category category = getCategoryById(categoryId);

        if (category == null) {
            return false;
        }

        categories.remove(category);

        return true;
    }
}
