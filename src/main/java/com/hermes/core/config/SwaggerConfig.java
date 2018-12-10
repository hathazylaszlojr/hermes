package com.hermes.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final Contact DEFAULT_CONTACT = new Contact("Laszlo Hathazy", "www.github.com", "hathazy.laszlo@yahoo.com");
    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Api Documentation", "Api Documentation", "1.0", "urn:tos",
            DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(Collections.singletonList(APPLICATION_JSON_VALUE));


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .select()
                    .paths(PathSelectors.ant("/api/v1/**"))
                .build();
    }
}