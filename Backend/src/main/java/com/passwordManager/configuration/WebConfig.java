package com.passwordManager.configuration;

import com.passwordManager.interceptors.VaultUnlockInterceptor;
import io.micrometer.common.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final VaultUnlockInterceptor vaultUnlockInterceptor;

    public WebConfig(@NonNull VaultUnlockInterceptor vaultUnlockInterceptor) {
        this.vaultUnlockInterceptor = vaultUnlockInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(vaultUnlockInterceptor);
    }
}
