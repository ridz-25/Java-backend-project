
package src.console;

import java.util.Scanner;
import src.service.UserService;
import src.service.CategoryService;
import src.service.GigService;
import src.service.OrderService;
import src.model.User;
import src.model.Buyer;
import src.model.Seller;

public class MainMenu {

    Scanner sc = new Scanner(System.in);
    UserService userService = new UserService();

CategoryService categoryService = new CategoryService();

GigService gigService = new GigService(userService, categoryService);

OrderService orderService = new OrderService(userService, gigService);

    public void displayMenu() {

        int choice = 0;

        do {
            System.out.println("\n================================");
            System.out.println("       WELCOME TO GIGCONNECT");
            System.out.println("================================");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("\nEnter a number for your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
case 1:
    System.out.println("\n===== USER REGISTRATION =====");

    String name = InputHelper.getString("Enter your name: ");
    String email = InputHelper.getString("Enter your email: ");
    String password = InputHelper.getString("Enter your password: ");
    String phone = InputHelper.getString("Enter your phone: ");

    System.out.println("\n1. Register as Buyer");
    System.out.println("2. Register as Seller");

    int role = InputHelper.getInt("Choose your role: ");

    try {

        if (role == 1) {

            String address = InputHelper.getString("Enter your address: ");

            Buyer buyer = userService.registerBuyer(
                name, email, password, phone, address
            );

            System.out.println("Buyer registered successfully!");
            System.out.println("Your User ID: " + buyer.getUserId());

        } else if (role == 2) {

            String bio = InputHelper.getString("Enter your bio: ");
            String skills = InputHelper.getString("Enter your skills: ");
            String experience = InputHelper.getString("Enter your experience: ");

            Seller seller = userService.registerSeller(
                name, email, password, phone, bio, skills, experience
            );

            System.out.println("Seller registered successfully!");
            System.out.println("Your User ID: " + seller.getUserId());

        } else {
            System.out.println("Invalid role!");
        }

    } catch (IllegalArgumentException e) {
        System.out.println("Registration failed: " + e.getMessage());
    }

    break;

                    case 2:
    System.out.println("\n===== USER LOGIN =====");

    String loginEmail = InputHelper.getString("Enter your email: ");
    String loginPassword = InputHelper.getString("Enter your password: ");

    if (loginEmail.trim().isEmpty() || loginPassword.trim().isEmpty()) {
        System.out.println("Email and password cannot be empty!");
        break;
    }

    User user = userService.login(loginEmail, loginPassword);

if (user == null) {
    System.out.println("Invalid email or password!");
    break;
}

System.out.println("\nLogin successful!");
System.out.println("Welcome, " + user.getName());

if (user instanceof Buyer) {
    System.out.println("Logged in as Buyer.");

} else if (user instanceof Seller) {
    System.out.println("Logged in as Seller.");
}
break;

                    case 3:
                        System.out.println("Thank you for using GigConnect!");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }

        } while (choice != 3);
    }
}
