package uz.pdp.inventorymanagementsystem.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Inventory Management System API")
                    .version("1.0")
                    .description("API documentation for Inventory Management System")
                    .contact(Contact().name("Support").email("support@wharehouse.com"))
            )
    }
}
