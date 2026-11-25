
package src.console;
import src.service.GigService;
import src.service.OrderService;
import src.model.User;
import src.model.Gig;
import src.model.Order;
import src.model.OrderStatus;
import java.util.List;
public class SellerMenu {
private GigService gigService;
private OrderService orderService;
private User loggedInUser;

public SellerMenu(GigService gigService,
                  OrderService orderService,
                  User loggedInUser) {

    this.gigService = gigService;
    this.orderService = orderService;
    this.loggedInUser = loggedInUser;
}
    public void displayMenu() {
        int choice;

        do {
            System.out.println("\n======= SELLER DASHBOARD =======");
            System.out.println("1. Create Gig");
            System.out.println("2. View My Gigs");
            System.out.println("3. Update Gig");
            System.out.println("4. Delete Gig");
            System.out.println("5. View Orders");
            System.out.println("6. Logout");

            choice = InputHelper.getInt("Enter your choice: ");

            switch (choice) {

               case 1:
    System.out.println("\n===== CREATE GIG =====");

    String title = InputHelper.getString("Enter gig title: ");

    String description = InputHelper.getString("Enter gig description: ");

    int categoryId = InputHelper.getInt("Enter category ID: ");

    double price = getPrice();

    String deliveryTime = InputHelper.getString(
        "Enter delivery time (e.g. 3 days): "
    );

    try {
        Gig gig = gigService.createGig(
            loggedInUser.getUserId(),
            title,
            description,
            categoryId,
            price,
            deliveryTime
        );

        System.out.println("\nGig created successfully!");
        System.out.println("Your Gig ID: " + gig.getGigId());

    } catch (IllegalArgumentException e) {
        System.out.println("Failed to create gig: " + e.getMessage());
    }

    break;

               case 2:
    List<Gig> myGigs = gigService.getGigsBySeller(
        loggedInUser.getUserId()
    );

    if (myGigs.isEmpty()) {
        System.out.println("You have not created any gigs yet.");
    } else {
        System.out.println("\n===== MY GIGS =====");

        for (Gig gig : myGigs) {
            System.out.println("--------------------------");
            System.out.println("Gig ID: " + gig.getGigId());
            System.out.println("Title: " + gig.getTitle());
            System.out.println("Description: " + gig.getDescription());
            System.out.println("Category ID: " + gig.getCategoryId());
            System.out.println("Price: Rs. " + gig.getPrice());
            System.out.println("Delivery: " + gig.getDeliveryTime());
            System.out.println("Status: " + gig.getStatus());
        }
    }
    break;

                case 3:
                    int updateId = InputHelper.getInt("Enter Gig ID to update: ");
                    System.out.println("Update request for Gig ID: " + updateId);
                    break;

                case 4:
                    int deleteId = InputHelper.getInt("Enter Gig ID to delete: ");
                    System.out.println("Delete request for Gig ID: " + deleteId);
                    break;

                case 5:
                    System.out.println("Displaying orders received...");
                    break;

                case 6:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);
    }

    private double getPrice() {
        while (true) {
            try {
                double price = Double.parseDouble(
                    InputHelper.getString("Enter gig price: ")
                );

                if (price <= 0) {
                    System.out.println("Price must be greater than zero!");
                } else {
                    return price;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price!");
            }
        }
    }
}
