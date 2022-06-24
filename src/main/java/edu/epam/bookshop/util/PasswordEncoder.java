package edu.epam.bookshop.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    private PasswordEncoder() {

    }

    public static String encode(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
