package com.itm.gestorfacturas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GestorfacturasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestorfacturasApplication.class, args);
	}

}
