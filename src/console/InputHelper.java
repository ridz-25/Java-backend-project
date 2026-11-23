
package src.console;

import java.util.Scanner;

public class InputHelper {

    private static Scanner sc = new Scanner(System.in);

    public static String getString(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public static int getInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }
}
