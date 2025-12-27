package com.passwordManager.controllers;

import com.passwordManager.security.crypto.KeyDerivationUtil;
import com.passwordManager.security.session.SessionKeyHolder;
import com.passwordManager.user.entity.User;
import com.passwordManager.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vault")
public class VaultController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/unlock")
    public ResponseEntity<Boolean> unlockVault(
            @RequestBody Map<String, String> payload,
            HttpSession session,
            Principal principal
    ) throws GeneralSecurityException {
        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow();
        if (!passwordEncoder.matches(payload.get("password"), user.getPassword()))
            return ResponseEntity.ok(false);

        SecretKey key = KeyDerivationUtil.deriveAesKey(
                payload.get("password").toCharArray(),
                user.getKdfSalt());

        session.setAttribute("VAULT_KEY",
                new SessionKeyHolder(key));

        session.setAttribute("VAULT_UNLOCKED_AT", Instant.now());
        return ResponseEntity.ok(true);
    }


    @PostMapping("/lock")
    public void lockVault(HttpSession session) {
        SessionKeyHolder sessionKeyHolder = (SessionKeyHolder) session.getAttribute("VAULT_KEY");
        if (sessionKeyHolder != null) {
            sessionKeyHolder.destroy();
        }
        session.removeAttribute("VAULT_KEY");
        session.removeAttribute("VAULT_UNLOCKED_AT");
    }
}
