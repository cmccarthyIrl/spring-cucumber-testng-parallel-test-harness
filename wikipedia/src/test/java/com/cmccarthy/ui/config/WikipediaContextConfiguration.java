package com.cmccarthy.ui.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
@ComponentScan({"com.cmccarthy.ui", "com.cmccarthy.common"})
@PropertySource("classpath:/application.properties")
public class WikipediaContextConfiguration {
}
