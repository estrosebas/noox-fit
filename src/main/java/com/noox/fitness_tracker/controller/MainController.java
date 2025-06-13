package com.noox.fitness_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import com.noox.fitness_tracker.model.Contact;
import com.noox.fitness_tracker.model.Promotion;
import java.util.List;
import java.util.Arrays;
import java.util.Locale;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
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
    public List<Promotion> getPromotions(Locale locale) {
        // Determine if we should use Spanish or English
        boolean isSpanish = locale.getLanguage().equals("es");

        return Arrays.asList(
            new Promotion(
                isSpanish ? "Especial de Verano" : "Summer Special",
                isSpanish ? "¡Ponte en forma para el verano! Descuento especial en membresías anuales" 
                         : "Get fit for summer! Special discount on annual memberships",
                "25%",
                "2025-08-31"
            ),
            new Promotion(
                isSpanish ? "Oferta para Nuevos Miembros" : "New Member Offer",
                isSpanish ? "¡Únete ahora y obtén tu primer mes gratis!" 
                         : "Join now and get your first month free",
                "100%",
                "2025-12-31"
            ),
            new Promotion(
                isSpanish ? "Trae un Amigo" : "Bring a Friend",
                isSpanish ? "Refiere a un amigo y ambos obtienen un descuento en planes mensuales" 
                         : "Refer a friend and both get a discount on monthly plans",
                "15%",
                "2025-06-30"
            )
        );
    }
}
