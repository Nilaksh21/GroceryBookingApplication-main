package com.gb.um.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                description = "Rest API's to Order and Manage Grocery Items in Store.",
                contact = @Contact(
                        name = "Nilaksh Saini",
                        email = "nilaksh0123@@gmail.com",
                        url = "https://www.linkedin.com/in/nilakshsaini/"
                ),
                title = "Grocery Booking Api",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local Environment : Grocery Booking Api",
                        url = "http://localhost:8088/"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Authorization Bearer Token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
}
