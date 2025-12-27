package com.miniproject.security.crypto;

public record EncryptionResult (
    byte[] encryptedPassword,
    byte[] iv
    ){}
