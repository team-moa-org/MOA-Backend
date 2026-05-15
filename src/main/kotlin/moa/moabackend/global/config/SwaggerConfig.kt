package moa.moabackend.global.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "MOA API",
        description = "MOA 백엔드 API 명세서입니다.",
        version = "v1"
    )
)
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        val securitySchemeName = "access-token"
        val securityRequirement = SecurityRequirement().addList(securitySchemeName)
        val components = Components().addSecuritySchemes(
            securitySchemeName,
            SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .`in`(SecurityScheme.In.HEADER)
        )

        return OpenAPI()
            .components(components)
    }
}
