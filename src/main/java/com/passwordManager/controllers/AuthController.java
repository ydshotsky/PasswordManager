package com.passwordManager.controllers;

import com.passwordManager.configuration.UserPrincipal;
import com.passwordManager.security.crypto.CryptoUtils;
import com.passwordManager.user.entity.User;
import com.passwordManager.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
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
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("User already exists");
        System.out.println("signing");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setKdfSalt(CryptoUtils.generateRandomSalt());
        try {
            userRepository.save(user);
            System.out.println("user saved");
            
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error saving user,please try again later");
        }
        return ResponseEntity.ok("user created successfully");
    }

    @DeleteMapping("/delete-all")
    @Transactional
    public ResponseEntity<Void> deleteProfile(@RequestBody Map<String, String> payload,
                                              Authentication authentication,
                                              HttpServletRequest request) throws ServletException {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        if (passwordEncoder.matches(payload.get("password"), user.getPassword())) {
            userRepository.delete(user);
            request.logout();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/delete-all")
    public String DeleteProfile() {
        return "deleteProfile";
    }
}
