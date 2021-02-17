package com.cmccarthy.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
@ComponentScan({
        "com.cmccarthy"
})
@PropertySource("classpath:/application.properties")
public class WeatherContextConfiguration {

}
