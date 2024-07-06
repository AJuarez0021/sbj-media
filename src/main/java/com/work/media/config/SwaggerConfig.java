package com.work.media.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class SwaggerConfig.
 *
 * @author ajuarez
 */
@Configuration
@Slf4j
public class SwaggerConfig {

    /**
     * Open API.
     *
     * @return the open API
     */
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Music-Converter/Download Video").version("1.0.0")
                .description("Aplicacion para convertrir de mp4 a mp3 y descarga de videos YT").contact(getContact()));
    }

    /**
     * Gets the contact.
     *
     * @return the contact
     */
    private Contact getContact() {
        return new Contact().name("Music-Converter")
                .url("https://www.music.com/")
                .email("music@mail.com");
    }
}
