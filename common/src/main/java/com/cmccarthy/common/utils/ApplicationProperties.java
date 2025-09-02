package com.cmccarthy.common.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration properties class that holds application-specific settings
 * loaded from application properties files.
 */
@Getter
@Component
public class ApplicationProperties {

    /**
     * The URL for the weather application API.
     */
    @Value("${weather.url.value}")
    @Setter
    private String weatherAppUrl;
    
    /**
     * The URL for the Wikipedia website.
     */
    @Value("${wikipedia.url.value}")
    @Setter
    private String wikipediaUrl;
    
    /**
     * The browser type to use for UI tests (e.g., chrome, firefox, edge).
     */
    @Value("${browser}")
    @Setter
    private String browser;
    
    /**
     * The URL for the Selenium Grid when running tests remotely.
     */
    @Value("${gridUrl}")
    private String gridUrl;
}