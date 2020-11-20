package com.minko.socket.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    NetHttpTransport netHttpTransport() {
        return new NetHttpTransport();
    }

    @Bean
    JacksonFactory jacksonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    GoogleIdTokenVerifier.Builder builder() {
        return new GoogleIdTokenVerifier.Builder(netHttpTransport(), jacksonFactory());
    }

}
