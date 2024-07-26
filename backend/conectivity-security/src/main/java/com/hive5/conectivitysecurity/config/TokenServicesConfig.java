package com.hive5.conectivitysecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class TokenServicesConfig {

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(3600); // 1 hour
        tokenServices.setRefreshTokenValiditySeconds(86400); // 1 day
        return tokenServices;
    }

    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }
}
