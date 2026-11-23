
package console;

public class SellerMenu {

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
                    double price = getPrice();

                    System.out.println("\nGig details collected successfully!");
                    System.out.println("Title: " + title);
                    System.out.println("Description: " + description);
                    System.out.println("Price: " + price);
                    break;

                case 2:
                    System.out.println("Displaying your gigs...");
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
