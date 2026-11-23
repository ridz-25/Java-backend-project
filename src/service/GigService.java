
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Gig;
import src.model.GigStatus;
import src.model.User;
import src.model.Seller;
import src.model.Category;
import src.util.ValidationUtil;

public class GigService {

    // Temporary in-memory storage
    private ArrayList<Gig> gigs = new ArrayList<>();

    // Automatically generate Gig IDs
    private int nextGigId = 1;

    // Shared service objects
    private UserService userService;
    private CategoryService categoryService;

    // Constructor injection
    public GigService(
            UserService userService,
            CategoryService categoryService) {

        this.userService = userService;
        this.categoryService = categoryService;
    }

    // Check whether user is a valid seller
    private boolean isValidSeller(int sellerId) {

        User user = userService.getUserById(sellerId);

        return user instanceof Seller && user.isActive();
    }

    // Check whether category exists
    private boolean isValidCategory(int categoryId) {

        Category category =
            categoryService.getCategoryById(categoryId);

        return category != null;
    }

    // Create new Gig
    public Gig createGig(
            int sellerId,
            String title,
            String description,
            int categoryId,
            double price,
            String deliveryTime) {

        if (!isValidSeller(sellerId)) {
            throw new IllegalArgumentException(
                "Invalid seller."
            );
        }

        if (ValidationUtil.isEmpty(title) ||
            ValidationUtil.isEmpty(description) ||
            ValidationUtil.isEmpty(deliveryTime)) {

            throw new IllegalArgumentException(
                "Gig details cannot be empty."
            );
        }

        if (price <= 0 || Double.isNaN(price) ||
            Double.isInfinite(price)) {

            throw new IllegalArgumentException(
                "Price must be greater than zero."
            );
        }

        if (!isValidCategory(categoryId)) {
            throw new IllegalArgumentException(
                "Category does not exist."
            );
        }

        Timestamp currentTime =
            new Timestamp(System.currentTimeMillis());

        Gig gig = new Gig(
            nextGigId++,
            title.trim(),
            description.trim(),
            sellerId,
            categoryId,
            price,
            deliveryTime.trim(),
            GigStatus.ACTIVE,
            currentTime,
            currentTime,
            0.0,
            0
        );

        gigs.add(gig);

        return gig;
    }

    // Find Gig by ID
    public Gig getGigById(int gigId) {

        for (Gig gig : gigs) {

            if (gig.getGigId() == gigId &&
                gig.getStatus() != GigStatus.DELETED) {

                return gig;
            }
        }

        return null;
    }

    // Get all active Gigs
    public List<Gig> getAllGigs() {

        ArrayList<Gig> activeGigs = new ArrayList<>();

        for (Gig gig : gigs) {

            if (gig.getStatus() == GigStatus.ACTIVE) {
                activeGigs.add(gig);
            }
        }

        return activeGigs;
    }

    // Get Gigs by Seller
    public List<Gig> getGigsBySeller(int sellerId) {

        ArrayList<Gig> sellerGigs = new ArrayList<>();

        for (Gig gig : gigs) {

            if (gig.getSellerId() == sellerId &&
                gig.getStatus() != GigStatus.DELETED) {

                sellerGigs.add(gig);
            }
        }

        return sellerGigs;
    }

    // Get Gigs by Category
    public List<Gig> getGigsByCategory(int categoryId) {

        ArrayList<Gig> categoryGigs = new ArrayList<>();

        for (Gig gig : gigs) {

            if (gig.getCategoryId() == categoryId &&
                gig.getStatus() == GigStatus.ACTIVE) {

                categoryGigs.add(gig);
            }
        }

        return categoryGigs;
    }

    // Search Gigs by title or description
    public List<Gig> searchGigs(String keyword) {

        ArrayList<Gig> matchingGigs = new ArrayList<>();

        if (ValidationUtil.isEmpty(keyword)) {
            return matchingGigs;
        }

        String search = keyword.trim().toLowerCase();

        for (Gig gig : gigs) {

            if (gig.getStatus() == GigStatus.ACTIVE &&
                (gig.getTitle().toLowerCase().contains(search) ||
                 gig.getDescription().toLowerCase().contains(search))) {

                matchingGigs.add(gig);
            }
        }

        return matchingGigs;
    }

    // Update Gig
    public boolean updateGig(
            int gigId,
            int sellerId,
            String title,
            String description,
            int categoryId,
            double price,
            String deliveryTime) {

        Gig gig = getGigById(gigId);

        if (gig == null ||
            gig.getSellerId() != sellerId ||
            !isValidSeller(sellerId)) {

            return false;
        }

        if (ValidationUtil.isEmpty(title) ||
            ValidationUtil.isEmpty(description) ||
            ValidationUtil.isEmpty(deliveryTime) ||
            price <= 0 || Double.isNaN(price) ||
            Double.isInfinite(price) ||
            !isValidCategory(categoryId)) {

            return false;
        }

        gig.setTitle(title.trim());
        gig.setDescription(description.trim());
        gig.setCategoryId(categoryId);
        gig.setPrice(price);
        gig.setDeliveryTime(deliveryTime.trim());
        gig.updateGig();

        return true;
    }

    // Delete Gig (Soft Delete)
    public boolean deleteGig(int gigId, int sellerId) {

        Gig gig = getGigById(gigId);

        if (gig == null ||
            gig.getSellerId() != sellerId) {

            return false;
        }

        gig.deleteGig();

        return true;
    }

    // Activate Gig
    public boolean activateGig(int gigId, int sellerId) {

        Gig gig = getGigById(gigId);

        if (gig == null ||
            gig.getSellerId() != sellerId ||
            !isValidSeller(sellerId)) {

            return false;
        }

        gig.activate();

        return true;
    }

    // Deactivate Gig
    public boolean deactivateGig(int gigId, int sellerId) {

        Gig gig = getGigById(gigId);

        if (gig == null ||
            gig.getSellerId() != sellerId) {

            return false;
        }

        gig.deactivate();

        return true;
    }
}
