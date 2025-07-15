package com.noox.fitness_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import com.noox.fitness_tracker.model.Contact;
import com.noox.fitness_tracker.model.Promotion;
import com.noox.fitness_tracker.dto.ContactoDTO;
import java.util.List;
import java.util.Arrays;
import java.util.Locale;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("contactoDTO", new ContactoDTO());
        return "index";
    }

    /*@GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }*/

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @PostMapping("/api/contact")
    @ResponseBody
    public String submitContact(@RequestBody Contact contact) {
        // Log the contact information to console
        System.out.println("Contact Form Submission: " + contact);
        return "success";
    }    
    @GetMapping("/api/promotions")
    @ResponseBody
    public List<Promotion> getPromotions() {
        return Arrays.asList(
            new Promotion(
                "promotion.summer.title",
                "promotion.summer.description",
                "25%",
                "2025-08-31"
            ),
            new Promotion(
                "promotion.new.member.title",
                "promotion.new.member.description",
                "100%",
                "2025-12-31"
            ),
            new Promotion(
                "promotion.bring.friend.title",
                "promotion.bring.friend.description",
                "15%",
                "2025-06-30"
            )
        );
    }
}
