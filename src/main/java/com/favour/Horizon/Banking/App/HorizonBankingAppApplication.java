package com.favour.Horizon.Banking.App;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Horizon Banking Application",
				description = "Backend REST APIs for Horizon Banking Application",
				version = "v.1.0",
				contact = @Contact(
						name = "Favour Ameh",
						email = "amehfavour77@gmail.com",
						url = "https://github.com/thefavourameh"
				),
				license = @License(
						name = "Horizon Banking Application",
						url = "https://github.com/thefavourameh"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Backend REST APIs for Horizon Banking Application",
				url = "https://github.com/thefavourameh"
		)
)
public class HorizonBankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(HorizonBankingAppApplication.class, args);
	}

}
