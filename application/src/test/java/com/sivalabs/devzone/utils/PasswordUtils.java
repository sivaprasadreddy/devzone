package com.sivalabs.devzone.utils;

import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<String> plainTextPwds = List.of("user");
        plainTextPwds.forEach(pwd -> {
            System.out.println("Plain Text: " + pwd);
            System.out.println("Encrypted: " + encoder.encode(pwd));
        });
    }
}
