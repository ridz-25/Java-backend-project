
package src.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash password before storing it in database
    public static String hashPassword(String password) {

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException(
                "Password cannot be empty"
            );
        }

        return BCrypt.hashpw(
            password,
            BCrypt.gensalt(12)
        );
    }

    // Verify entered password during login
    public static boolean verifyPassword(
            String password, String hashedPassword) {

        if (password == null || hashedPassword == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(
                password,
                hashedPassword
            );

        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
