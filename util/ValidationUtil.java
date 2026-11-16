
package util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // Email validation pattern
    private static final String EMAIL_REGEX =
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Phone validation pattern
    private static final String PHONE_REGEX =
        "^\\+?[0-9]{10,15}$";

    // Validate email
    public static boolean isValidEmail(String email) {

        if (isEmpty(email)) {
            return false;
        }

        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Validate password
    public static boolean isValidPassword(String password) {

        if (isEmpty(password)) {
            return false;
        }

        // Minimum 8 characters
        // At least one uppercase letter
        // At least one lowercase letter
        // At least one digit
        // At least one special character

        String passwordRegex =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)"
            + "(?=.*[@#$%^&+=!]).{8,}$";

        return Pattern.matches(passwordRegex, password);
    }

    // Validate phone number
    public static boolean isValidPhone(String phone) {

        if (isEmpty(phone)) {
            return false;
        }

        return Pattern.matches(PHONE_REGEX, phone);
    }

    // Validate rating (1 to 5)
    public static boolean isValidRating(int rating) {

        return rating >= 1 && rating <= 5;
    }

    // Check empty or blank string
    public static boolean isEmpty(String value) {

        return value == null || value.trim().isEmpty();
    }
}
