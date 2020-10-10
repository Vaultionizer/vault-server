package com.vaultionizer.vaultserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class VaultserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaultserverApplication.class, args);
	}

}
