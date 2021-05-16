package com.vaultionizer.vaultserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
public class VaultserverApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(VaultserverApplication.class);
        app.setAdditionalProfiles("ssl");
        app.run(args);
    }

}
