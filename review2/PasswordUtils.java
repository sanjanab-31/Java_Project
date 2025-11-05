package review2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple password hashing utility using SHA-256.
 * Note: For production use prefer BCrypt (e.g., jBCrypt) with salt.
 */
public class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    public static boolean verifyPassword(String plain, String hashed) {
        return hashPassword(plain).equals(hashed);
    }
}
