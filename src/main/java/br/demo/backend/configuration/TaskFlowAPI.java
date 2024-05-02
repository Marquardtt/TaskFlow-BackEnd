package br.demo.backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springdoc.webmvc.ui.SwaggerConfigResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//this sets the springdoc swagger ui title, description and version
public class TaskFlowAPI {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("TaskFlow API")
                        .version("1.0")
                        .description("Thats is the API for the TaskFlow, a software to manage your groups, projects, tasks and properties. You are invited to use it! Enjoy!")
                        .summary("TaskFlow API"));
    }

}
