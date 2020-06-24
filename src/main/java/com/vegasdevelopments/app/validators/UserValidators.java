package com.vegasdevelopments.app.validators;

import java.util.regex.Pattern;

public class UserValidators {
    private UserValidators() {
    }

    public static boolean emailValidator(String email) {
        final String pattern = "^[\\w-_]+(\\.[\\w-_]+)*@[a-z]{2,}(\\.[a-z]{2,6})*(\\.[a-z]{2,6})$";
        return Pattern.matches(pattern, email.toLowerCase());
    }

    public static boolean passwordValidator(String password) {
        return password.length() >= 8;
    }
}
