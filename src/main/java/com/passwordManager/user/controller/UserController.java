package com.passwordManager.user.controller;

import com.passwordManager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/username-availability")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
        return ResponseEntity.ok(!userRepository.existsByUsername(username));
    }

}
