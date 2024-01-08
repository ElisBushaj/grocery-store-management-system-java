package com.grc.GroceryStore.Utils;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {
    public static String generateHashedPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    public static boolean verifyPassword(String enteredPassword, String hashedPassword) {
        return BCrypt.checkpw(enteredPassword, hashedPassword);
    }
}