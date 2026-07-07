
package utils;

public class ValidationUtils {

    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }

    public static void validatePositiveInteger(String value, String fieldName) {
        try {
            int number = Integer.parseInt(value);
            if (number <= 0) {
                throw new IllegalArgumentException(fieldName + " must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid integer");
        }
    }

    public static void validatePositiveDouble(String value, String fieldName) {
        try {
            double number = Double.parseDouble(value);
            if (number < 0 || number > 4.0) {
                throw new IllegalArgumentException(fieldName + " must be between 0 and 4.0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    public static void validateEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }

    public static void validatePhoneNumber(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("^[0-9]{10}$")) {
                throw new IllegalArgumentException("Phone number must be 10 digits");
            }
        }
    }
}