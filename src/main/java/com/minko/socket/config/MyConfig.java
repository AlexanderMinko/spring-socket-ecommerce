package com.minko.socket.config;

import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {

    @Bean
    NetHttpTransport netHttpTransport() {
        return new NetHttpTransport();
    }

}
