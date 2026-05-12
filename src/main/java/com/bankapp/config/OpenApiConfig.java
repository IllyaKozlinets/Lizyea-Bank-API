package com.bankapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

import static javax.print.attribute.standard.ReferenceUriSchemesSupported.HTTP;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Digital Banking API",
        version = "1.0",
        description = "Secure banking backend with JWT authentication",
        contact = @Contact(
        name = "Illia Kozlynets",
        email = "illyakoz2003@gmail.com"
)))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

public class OpenApiConfig {

}
