package com.furiosaming.kanban.web.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConf {

    @Bean
    public OpenAPI springDemoOpenAPI(){
        return new OpenAPI().info(new Info().title("Kanban Task Manager App API")
                .description("Workflow visualization tool")
                .version("v1.0.0")
                .license(new License().name("StreletsA").url("furiosaming@mail.ru")))
                .externalDocs(new ExternalDocumentation()
                        .description("Kanban Task Manager Wiki Documentation")
                        .url("furiosaming@mail.ru"));
    }

    @Bean
    public GroupedOpenApi demoApi() {
        return GroupedOpenApi.builder()
                .group("demo-public-api")
                .pathsToMatch("/rest/**", "/**")
                .build();
    }

}
