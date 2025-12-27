package com.miniproject.controllers;

import com.miniproject.utils.CryptoUtils;
import com.miniproject.entities.User;
import com.miniproject.repositories.PasswordRepository;
import com.miniproject.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }
    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> signUp(@ModelAttribute User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setKdfSalt(CryptoUtils.generateRandomSalt());
            userRepository.save(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/")
    public String Dashboard(Model model, Principal principal) {
        String username = principal.getName();
        long passwordCount = passwordRepository.countByUserUsername(username);
        model.addAttribute("username", username);
        model.addAttribute("passwordCount", passwordCount);
        return "Dashboard";
    }


    @DeleteMapping("/delete-all")
    @Transactional
    public String deleteProfile(Principal principal) {
        String username = principal.getName();
        passwordRepository.deleteAllByUserUsername(username);
        userRepository.deleteByUsername(username);
        return "redirect:/login";
    }

    @GetMapping("delete-all")
    public String DeleteProfile() {
        return "deleteProfile";
    }
}
