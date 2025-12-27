package com.miniproject.user.controller;

import com.miniproject.user.repository.UserRepository;
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
