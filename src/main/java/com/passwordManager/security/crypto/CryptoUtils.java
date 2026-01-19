package com.passwordManager.security.crypto;

import java.security.SecureRandom;

public class CryptoUtils {
    private static final int IV_LENGTH=12;
    private static final int SALT_LENGTH = 16;
    private static final SecureRandom secureRandom = new SecureRandom();
    public static byte[] generateRandomSalt() {
        
        System.out.println("generating salt");
        byte[] salt = new byte[SALT_LENGTH];
        try{
        secureRandom.reseed();
        secureRandom.nextBytes(salt);}
        catch(Exception e){
            
        System.out.println(e);
        }
        return salt;
    }
    public static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.reseed();
        secureRandom.nextBytes(iv);
        return iv;
    }
}
