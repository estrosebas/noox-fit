package com.noox.fitness_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import com.noox.fitness_tracker.model.LoginRequest;
import com.noox.fitness_tracker.model.RegisterRequest;

@Controller
public class AuthController {

    @PostMapping("/api/auth/login")
    @ResponseBody
    public String login(@RequestBody LoginRequest loginRequest) {
        // Log the login attempt to console (without password)
        System.out.println("Login Attempt: " + loginRequest);
        // In a real application, you would validate credentials here
        return "success";
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public String register(@RequestBody RegisterRequest registerRequest) {
        // Log the registration attempt to console (without password)
        System.out.println("Registration Attempt: " + registerRequest);
        // Validate terms acceptance
        if (!registerRequest.isTermsAccepted()) {
            return "error: terms not accepted";
        }
        // In a real application, you would save the user to database here
        return "success";
    }
}