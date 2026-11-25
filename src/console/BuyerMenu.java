
package src.console;
import src.service.GigService;
import src.service.OrderService;
import src.model.User;
import src.model.Gig;
import src.model.Order;
import java.util.List;

public class BuyerMenu {
    private GigService gigService;
private OrderService orderService;
private User loggedInUser;

public BuyerMenu(GigService gigService,
                 OrderService orderService,
                 User loggedInUser) {

    this.gigService = gigService;
    this.orderService = orderService;
    this.loggedInUser = loggedInUser;
}

    public void displayMenu() {
        int choice;

        do {
            System.out.println("\n======= BUYER DASHBOARD =======");
            System.out.println("1. View All Gigs");
            System.out.println("2. Search Gigs");
            System.out.println("3. View My Orders");
            System.out.println("4. Place Order");
            System.out.println("5. Logout");

            choice = InputHelper.getInt("Enter your choice: ");

            switch (choice) {
               case 1:
    List<Gig> gigs = gigService.getAllGigs();

    if (gigs.isEmpty()) {
        System.out.println("No gigs available!");
    } else {
        System.out.println("\n===== AVAILABLE GIGS =====");

        for (Gig gig : gigs) {
            System.out.println("--------------------------");
            System.out.println("Gig ID: " + gig.getGigId());
            System.out.println("Title: " + gig.getTitle());
            System.out.println("Description: " + gig.getDescription());
            System.out.println("Price: Rs. " + gig.getPrice());
            System.out.println("Delivery: " + gig.getDeliveryTime());
        }
    }
    break;

                case 2:
    String keyword = InputHelper.getString(
        "Enter keyword to search gigs: "
    );

    List<Gig> searchResults = gigService.searchGigs(keyword);

    if (searchResults.isEmpty()) {
        System.out.println("No matching gigs found!");
    } else {
        System.out.println("\n===== SEARCH RESULTS =====");

        for (Gig gig : searchResults) {
            System.out.println("--------------------------");
            System.out.println("Gig ID: " + gig.getGigId());
            System.out.println("Title: " + gig.getTitle());
            System.out.println("Description: " + gig.getDescription());
            System.out.println("Price: Rs. " + gig.getPrice());
        }
    }
    break;

               case 3:
    List<Order> orders = orderService.getOrdersByBuyer(
        loggedInUser.getUserId()
    );

    if (orders.isEmpty()) {
        System.out.println("You have not placed any orders yet.");
    } else {
        System.out.println("\n===== MY ORDERS =====");

        for (Order order : orders) {
            System.out.println("--------------------------");
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Gig ID: " + order.getGigId());
            System.out.println("Amount: Rs. " + order.getAmount());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Notes: " + order.getNotes());
        }
    }
    break;

                case 4:
    int gigId = InputHelper.getInt(
        "Enter Gig ID to place order: "
    );

    String notes = InputHelper.getString(
        "Enter order requirements: "
    );

    try {
        Order order = orderService.placeOrder(
            loggedInUser.getUserId(),
            gigId,
            notes
        );

        System.out.println("\nOrder placed successfully!");
        System.out.println("Your Order ID: " + order.getOrderId());
        System.out.println("Amount: Rs. " + order.getAmount());
        System.out.println("Status: " + order.getStatus());

    } catch (IllegalArgumentException e) {
        System.out.println("Order failed: " + e.getMessage());
    }

    break;
                case 5:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);
    }
}
