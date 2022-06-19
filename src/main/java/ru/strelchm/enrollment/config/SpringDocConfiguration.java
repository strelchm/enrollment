package ru.strelchm.enrollment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
  @Bean
  OpenAPI apiInfo() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Mega Market Open API")
                .description("Вступительное задание в Летнюю Школу Бэкенд Разработки Яндекса 2022")
                .version("1.0")
        )
        ;
  }
}