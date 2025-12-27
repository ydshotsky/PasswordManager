package com.miniproject.security.session;

import lombok.Getter;

import javax.crypto.SecretKey;

@Getter
public class SessionKeyHolder {
    private SecretKey secretKey;

    public SessionKeyHolder(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public void destroy() {
        if (secretKey != null) {
            byte[]encoded=secretKey.getEncoded();
            if (encoded != null) {
                java.util.Arrays.fill(encoded, (byte)0);
            }
        }
        secretKey = null;
    }
}
