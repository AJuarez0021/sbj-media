package com.work.media.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.schild.jave.Encoder;

/**
 *
 * @author linux
 */
@Configuration
public class ConverterConfig {

    @Bean
    public Encoder enconder() {
        return new Encoder();
    }
}
