package com.passwordManager.security.crypto;

public record EncryptionResult (
    byte[] encryptedPassword,
    byte[] iv
    ){}
