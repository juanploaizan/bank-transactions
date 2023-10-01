package com.transaccionesbancarias;

import com.transaccionesbancarias.model.ERole;
import com.transaccionesbancarias.model.Role;
import com.transaccionesbancarias.repository.RoleRepository;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransaccionesBancariasApplication {

	@Bean
	public OpenAPI customOpenAPI() {

		return new OpenAPI()
				.info(new io.swagger.v3.oas.models.info.Info()
						.title("Transacciones Bancarias API")
						.version("1.0")
						.description("Transacciones Bancarias API, desarrollada con Spring Boot 3.1.4 y Springdoc-openapi-ui 2.2.0")
						.license(new io.swagger.v3.oas.models.info.License()
								.name("Apache 2.0")
								.url("https://springdoc.org"))
						.termsOfService("https://swagger.io/terms/")
				);
	}

	public static void main(String[] args) {
		SpringApplication.run(TransaccionesBancariasApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(RoleRepository roleRepository) {
		return args -> {
			roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).build());
			roleRepository.save(Role.builder().name(ERole.ROLE_USER).build());
		};
	}

}
