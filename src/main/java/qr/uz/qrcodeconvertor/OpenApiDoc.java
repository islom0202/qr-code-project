package qr.uz.qrcodeconvertor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Islom",
                        email = "turgunpolatovislom5@gmail.com"
                ),
                description = "FinSight`s Api document",
                title = "FinSight`s Api Doc",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = @Server(
                description = "Local En"
        )
)
public class OpenApiDoc {

}