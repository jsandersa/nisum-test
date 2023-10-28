package com.jsanders.nisum.test;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }

  @Bean
  public OpenAPI miOpenApi() {
    return new OpenAPI()
            .info(new Info()
                    .title("Nisum - Spring Boot v.3 API")
                    .version("1.0")
                    .description("Test developed by Jorge Sanders A. in OpenAPI v.3")
                    .termsOfService("http://swagger.io/terms")
                    .license(new License().name("Apache 2.0")
                            .url("http://spring.org")));
  }
}
