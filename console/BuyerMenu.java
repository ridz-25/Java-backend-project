
package console;

public class BuyerMenu {

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
                    System.out.println("Displaying all available gigs...");
                    break;

                case 2:
                    String keyword = InputHelper.getString(
                        "Enter keyword to search gigs: "
                    );
                    System.out.println("Searching for: " + keyword);
                    break;

                case 3:
                    System.out.println("Displaying your orders...");
                    break;

                case 4:
                    int gigId = InputHelper.getInt(
                        "Enter Gig ID to place order: "
                    );
                    System.out.println("Order request for Gig ID: " + gigId);
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
