
package console;

import java.util.Scanner;

public class MainMenu {

    Scanner sc = new Scanner(System.in);

    public void displayMenu() {

        int choice = 0;

        do {
            System.out.println("\n================================");
            System.out.println("       WELCOME TO GIGCONNECT");
            System.out.println("================================");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.print("\nEnter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
case 1:
    System.out.println("\n===== USER REGISTRATION =====");

    String name, email, password;

    do {
        name = InputHelper.getString("Enter your name: ");
        if (name.trim().isEmpty()) {
            System.out.println("Name cannot be empty!");
        }
    } while (name.trim().isEmpty());

    do {
        email = InputHelper.getString("Enter your email: ");
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Please enter a valid email!");
        }
    } while (!email.contains("@") || !email.contains("."));

    do {
        password = InputHelper.getString("Enter your password: ");
        if (password.length() < 6) {
            System.out.println("Password must contain at least 6 characters!");
        }
    } while (password.length() < 6);

    System.out.println("\nRegistration details collected successfully!");
    break;

                    case 2:
    System.out.println("\n===== USER LOGIN =====");

    String loginEmail = InputHelper.getString("Enter your email: ");
    String loginPassword = InputHelper.getString("Enter your password: ");

    if (loginEmail.trim().isEmpty() || loginPassword.trim().isEmpty()) {
        System.out.println("Email and password cannot be empty!");
        break;
    }

    System.out.println("\nLogin details collected successfully!");

    System.out.println("\n1. Login as Buyer");
    System.out.println("2. Login as Seller");

    int role = InputHelper.getInt("Choose your role: ");

    if (role == 1) {
        new BuyerMenu().displayMenu();
    } else if (role == 2) {
        new SellerMenu().displayMenu();
    } else {
        System.out.println("Invalid role!");
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
