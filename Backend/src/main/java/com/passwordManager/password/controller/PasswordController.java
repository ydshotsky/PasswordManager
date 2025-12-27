package com.passwordManager.password.controller;

import com.passwordManager.annotations.VaultUnlockedRequired;
import com.passwordManager.configuration.UserPrincipal;
import com.passwordManager.password.dto.PasswordDto;
import com.passwordManager.user.entity.User;
import com.passwordManager.password.entity.VaultPassword;
import com.passwordManager.password.dto.SavePasswordRequest;
import com.passwordManager.password.mapper.PasswordEntityMapper;
import com.passwordManager.password.repository.PasswordRepository;
import com.passwordManager.user.repository.UserRepository;
import com.passwordManager.security.crypto.EncryptionResult;
import com.passwordManager.security.session.SessionKeyHolder;
import com.passwordManager.security.crypto.AesGcmUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/password/")
public class PasswordController {
    private final PasswordRepository passwordRepository;
    private final PasswordEntityMapper passwordEntityMapper;
    private final UserRepository userRepository;

    public SessionKeyHolder getActiveVaultKey(HttpSession session) {
        SessionKeyHolder sessionKeyHolder = (SessionKeyHolder) session.getAttribute("VAULT_KEY");
        return sessionKeyHolder != null && sessionKeyHolder.getSecretKey() != null ? sessionKeyHolder : null;
    }


    @VaultUnlockedRequired
    @PostMapping("/reveal/{id}")
    public ResponseEntity<String> revealPassword(
            @PathVariable Long id,
            HttpSession session,
            Principal principal
    ) throws GeneralSecurityException {
        SessionKeyHolder sessionKeyHolder = getActiveVaultKey(session);
        if (sessionKeyHolder == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("vault is locked, please enter master key");
        }

        VaultPassword password = passwordRepository.findById(id).orElseThrow();
        if (!password.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String decryptedPassword = AesGcmUtil.decrypt(
                password.getEncryptedPassword(),
                password.getIv(),
                sessionKeyHolder.getSecretKey()
        );
        return ResponseEntity.ok(decryptedPassword);
    }

    @GetMapping("/create-password-entry")
    public String passwordEntryForm(Model model) {
        model.addAttribute("passwordRequest", new SavePasswordRequest());
        return "password-entry-form";
    }
    

    @PostMapping("/save-vault-password")
    @Transactional
    @VaultUnlockedRequired
    public ResponseEntity<String> savePassword(
            @ModelAttribute SavePasswordRequest savePasswordRequest,
            HttpSession session,
            Authentication authentication) throws GeneralSecurityException {
        SessionKeyHolder sessionKeyHolder = getActiveVaultKey(session);
        if (sessionKeyHolder == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("vault is locked, please enter master key");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        VaultPassword vaultPassword = VaultPassword
                        .builder()
                        .siteUrl(savePasswordRequest.getSiteUrl())
                        .siteUsername(savePasswordRequest.getSiteUsername())
                        .phoneNumber(savePasswordRequest.getPhoneNumber())
                        .createdAt(savePasswordRequest.getCreatedAt())
                        .email(savePasswordRequest.getEmail())
                        .notes(savePasswordRequest.getNotes())
                        .build();

        User user = userRepository.findByUsername(userPrincipal.getUsername()).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        vaultPassword.setUser(user);


        SecretKey key = sessionKeyHolder.getSecretKey();
        EncryptionResult encryptionResult = AesGcmUtil.encrypt(savePasswordRequest.getPassword().getBytes(), key);
        vaultPassword.setEncryptedPassword(encryptionResult.encryptedPassword());
        vaultPassword.setIv(encryptionResult.iv());

        try {
            passwordRepository.save(vaultPassword);
        } catch (Exception e) {
            System.out.println("error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
        return ResponseEntity.ok("password saved successfully");
    }

    @GetMapping("/password-list")
    public String passwordListForm(Model model, Authentication authentication) {
        List<VaultPassword> passwords = passwordRepository.findByUserUsername(authentication.getName());
        List<PasswordDto> passwordDtos = passwords.stream()
                .map(passwordEntityMapper::getPasswordDto)
                .toList();
        model.addAttribute("passwords", passwordDtos);
        return "password-list";
    }

    @GetMapping("/search-password")
    public String searchPassword(@RequestParam(value = "keyword", required = false) String keyword, Model model, Authentication authentication) {
        if (keyword == null || keyword.trim().isEmpty()) {
            model.addAttribute("message", "Please enter a keyword to search.");
            return "search-password";
        }
        List<VaultPassword> passwords = passwordRepository.findByKeywordAndUserUsername(keyword, authentication.getName());
        List<PasswordDto> passwordDtos = passwords
                .stream()
                .map(passwordEntityMapper::getPasswordDto)
                .toList();
        if (passwords.isEmpty()) {
            model.addAttribute("message", "No passwords found for '" + keyword + "'");
        } else {
            model.addAttribute("passwords", passwordDtos);
        }
        return "search-password";
    }



    @DeleteMapping("/delete/{id}")
    @VaultUnlockedRequired
    public ResponseEntity<String> deletePassword(@PathVariable("id") Long id,HttpSession session) {
        SessionKeyHolder sessionKeyHolder = getActiveVaultKey(session);
        if (sessionKeyHolder == null) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("vault is locked, please enter login password");
        }
        passwordRepository.deleteById(id);
        return ResponseEntity.ok("password deleted successfully");
    }
}
