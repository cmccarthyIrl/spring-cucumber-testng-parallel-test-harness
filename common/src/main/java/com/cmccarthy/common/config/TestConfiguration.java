package com.cmccarthy.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "test")
public class TestConfiguration {

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

    public static class ParallelExecution {
        private boolean enabled = true;
        private int threadPoolSize = 4;
        private int dataProviderThreadCount = 4;
        
        // getters and setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getThreadPoolSize() { return threadPoolSize; }
        public void setThreadPoolSize(int threadPoolSize) { this.threadPoolSize = threadPoolSize; }
        public int getDataProviderThreadCount() { return dataProviderThreadCount; }
        public void setDataProviderThreadCount(int dataProviderThreadCount) { this.dataProviderThreadCount = dataProviderThreadCount; }
    }

    public static class ApiConfig {
        private int connectionTimeout = 30000;
        private int socketTimeout = 30000;
        private int maxRetries = 3;
        private boolean logRequestResponse = false;
        
        // getters and setters
        public int getConnectionTimeout() { return connectionTimeout; }
        public void setConnectionTimeout(int connectionTimeout) { this.connectionTimeout = connectionTimeout; }
        public int getSocketTimeout() { return socketTimeout; }
        public void setSocketTimeout(int socketTimeout) { this.socketTimeout = socketTimeout; }
        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
        public boolean isLogRequestResponse() { return logRequestResponse; }
        public void setLogRequestResponse(boolean logRequestResponse) { this.logRequestResponse = logRequestResponse; }
    }

    public static class UiConfig {
        private boolean headless = false;
        private int implicitWait = 10;
        private int pageLoadTimeout = 30;
        private String windowSize = "1920x1080";
        private boolean enableVideoRecording = false;
        
        // getters and setters
        public boolean isHeadless() { return headless; }
        public void setHeadless(boolean headless) { this.headless = headless; }
        public int getImplicitWait() { return implicitWait; }
        public void setImplicitWait(int implicitWait) { this.implicitWait = implicitWait; }
        public int getPageLoadTimeout() { return pageLoadTimeout; }
        public void setPageLoadTimeout(int pageLoadTimeout) { this.pageLoadTimeout = pageLoadTimeout; }
        public String getWindowSize() { return windowSize; }
        public void setWindowSize(String windowSize) { this.windowSize = windowSize; }
        public boolean isEnableVideoRecording() { return enableVideoRecording; }
        public void setEnableVideoRecording(boolean enableVideoRecording) { this.enableVideoRecording = enableVideoRecording; }
    }

    // Main getters and setters
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public int getThreadCount() { return threadCount; }
    public void setThreadCount(int threadCount) { this.threadCount = threadCount; }
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public boolean isTakeScreenshotOnFailure() { return takeScreenshotOnFailure; }
    public void setTakeScreenshotOnFailure(boolean takeScreenshotOnFailure) { this.takeScreenshotOnFailure = takeScreenshotOnFailure; }
    public boolean isEnableDetailedReporting() { return enableDetailedReporting; }
    public void setEnableDetailedReporting(boolean enableDetailedReporting) { this.enableDetailedReporting = enableDetailedReporting; }
    public String getDefaultBrowser() { return defaultBrowser; }
    public void setDefaultBrowser(String defaultBrowser) { this.defaultBrowser = defaultBrowser; }
    public ParallelExecution getParallelExecution() { return parallelExecution; }
    public void setParallelExecution(ParallelExecution parallelExecution) { this.parallelExecution = parallelExecution; }
    public ApiConfig getApi() { return api; }
    public void setApi(ApiConfig api) { this.api = api; }
    public UiConfig getUi() { return ui; }
    public void setUi(UiConfig ui) { this.ui = ui; }
}
