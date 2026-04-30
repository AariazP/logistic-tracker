package com.vcsoft.logistic_tracker_back.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OpenApiConfig tests")
class OpenApiConfigTest {

    @Test
    @DisplayName("customOpenAPI configures info and bearer security")
    void customOpenApi_containsExpectedMetadata() {
        OpenApiConfig config = new OpenApiConfig();

        OpenAPI api = config.customOpenAPI();

        assertThat(api.getInfo().getTitle()).isEqualTo("SmartShip API");
        assertThat(api.getInfo().getVersion()).isEqualTo("v1");
        assertThat(api.getInfo().getDescription()).contains("Logistic Tracker backend");
        assertThat(api.getSecurity()).isNotEmpty();
        assertThat(api.getSecurity().get(0).containsKey("bearerAuth")).isTrue();
        assertThat(api.getComponents().getSecuritySchemes().get("bearerAuth").getType())
                .isEqualTo(SecurityScheme.Type.HTTP);
        assertThat(api.getComponents().getSecuritySchemes().get("bearerAuth").getScheme())
                .isEqualTo("bearer");
        assertThat(api.getComponents().getSecuritySchemes().get("bearerAuth").getBearerFormat())
                .isEqualTo("JWT");
    }
}
