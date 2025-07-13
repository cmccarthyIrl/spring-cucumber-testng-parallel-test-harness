package com.cmccarthy.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "test")
public class TestConfiguration {

    // Main getters and setters
    private int maxRetries = 3;
    private int threadCount = 4;
    private int timeoutSeconds = 30;
    private boolean takeScreenshotOnFailure = true;
    private boolean enableDetailedReporting = true;
    private String defaultBrowser = "chrome";
    
    // Parallel execution settings
    private ParallelExecution parallelExecution = new ParallelExecution();
    
    // API testing settings
    private ApiConfig api = new ApiConfig();
    
    // UI testing settings
    private UiConfig ui = new UiConfig();

    @Setter
    @Getter
    public static class ParallelExecution {
        // getters and setters
        private boolean enabled = true;
        private int threadPoolSize = 4;
        private int dataProviderThreadCount = 4;
    }

    @Setter
    @Getter
    public static class ApiConfig {
        // getters and setters
        private int connectionTimeout = 30000;
        private int socketTimeout = 30000;
        private int maxRetries = 3;
        private boolean logRequestResponse = false;
    }

    @Setter
    @Getter
    public static class UiConfig {
        // getters and setters
        private boolean headless = true;
        private int implicitWait = 10;
        private int pageLoadTimeout = 30;
        private String windowSize = "1920x1080";
        private boolean enableVideoRecording = false;
    }

}
