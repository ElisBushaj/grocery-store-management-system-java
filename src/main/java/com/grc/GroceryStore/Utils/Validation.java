package com.grc.GroceryStore.Utils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Validation {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    private static final String NAME_REGEX = "^[A-Za-z]+$";
    private static final String ROLE_REGEX = "^(admin|cashier)$";

    public static boolean isValidName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidRole(String role) {
        Pattern pattern = Pattern.compile(ROLE_REGEX);
        Matcher matcher = pattern.matcher(role);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        if (email.isEmpty() || email.length() > 100) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateUserInput(String name, String lastname, String email, String role) {
        return isValidName(name) && isValidName(lastname) && isValidRole(role) && isValidEmail(email);
    }
}
