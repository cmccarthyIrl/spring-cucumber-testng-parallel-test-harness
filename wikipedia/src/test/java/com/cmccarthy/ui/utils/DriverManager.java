package com.cmccarthy.ui.utils;

import com.cmccarthy.common.config.TestConfiguration;
import com.cmccarthy.common.utils.ApplicationProperties;
import com.cmccarthy.common.utils.Constants;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> userDataDirThreadLocal = new ThreadLocal<>();
    private final Logger log = LoggerFactory.getLogger(DriverManager.class);

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private DriverWait driverWait;
    @Autowired
    private Environment environment;
    @Autowired(required = false)
    private TestConfiguration testConfiguration;

    public void createDriver() throws IOException {
        if (getDriver() == null) {
            log.info("Creating WebDriver for browser: {}", applicationProperties.getBrowser());
            
            if (Arrays.toString(this.environment.getActiveProfiles()).contains("cloud-provider")) {
                setRemoteDriver(new URL(applicationProperties.getGridUrl()));
            } else {
                setLocalWebDriver();
            }
            
            configureDriver();
        }
    }

    public void setLocalWebDriver() throws IOException {
        String browser = applicationProperties.getBrowser();
        log.info("Creating local {} driver", browser);
        
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                driverThreadLocal.set(new ChromeDriver(getChromeOptions()));
            }
            case "firefox" -> {
                driverThreadLocal.set(new FirefoxDriver(getFirefoxOptions()));
            }
            case "ie" -> {
                InternetExplorerOptions capabilitiesIE = new InternetExplorerOptions();
                capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                driverThreadLocal.set(new InternetExplorerDriver(capabilitiesIE));
            }
            case "safari" -> {
                driverThreadLocal.set(new SafariDriver(getSafariOptions()));
            }
            case "edge" -> {
                driverThreadLocal.set(new EdgeDriver(getEdgeOptions()));
            }
            default ->
                    throw new NoSuchElementException("Failed to create an instance of WebDriver for: " + browser);
        }
        
        setupDriverWait();
    }

    private void setRemoteDriver(URL hubUrl) {
        String browser = applicationProperties.getBrowser();
        log.info("Creating remote {} driver at {}", browser, hubUrl);
        
        Capabilities capability;
        switch (browser.toLowerCase()) {
            case "firefox" -> {
                capability = getFirefoxOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            case "chrome" -> {
                ChromeOptions options = getChromeOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, options));
            }
            case "ie" -> {
                capability = new InternetExplorerOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            case "safari" -> {
                capability = getSafariOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            case "edge" -> {
                capability = getEdgeOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            default ->
                    throw new NoSuchElementException("Failed to create an instance of RemoteWebDriver for: " + browser);
        }
        
        setupDriverWait();
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Essential arguments for stability and parallel execution
        options.addArguments("--disable-logging");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--remote-debugging-port=0"); // Use random available port
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        
        // Create unique user data directory for parallel execution
        String uniqueUserDataDir = System.getProperty("java.io.tmpdir") + 
            "chrome_user_data_" + Thread.currentThread().getId() + "_" + System.currentTimeMillis();
        options.addArguments("--user-data-dir=" + uniqueUserDataDir);
        
        // Store the user data directory path for cleanup later
        userDataDirThreadLocal.set(uniqueUserDataDir);
        
        // Prevent Chrome from creating crash dumps
        options.addArguments("--disable-crash-reporter");
        options.addArguments("--disable-in-process-stack-traces");
        
        if (isHeadlessMode()) {
            options.addArguments("--headless=new");
        }
        
        if (testConfiguration != null) {
            String windowSize = testConfiguration.getUi().getWindowSize();
            if (windowSize != null && !windowSize.isEmpty()) {
                options.addArguments("--window-size=" + windowSize);
            }
        }

        // Performance optimizations
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.managed_default_content_settings.images", 2); // Block images for faster loading
        options.setExperimentalOption("prefs", prefs);
        
        // Additional performance settings
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        return options;
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("marionette", true);
        
        if (isHeadlessMode()) {
            options.addArguments("--headless");
        }
        
        return options;
    }

    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-logging");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        if (isHeadlessMode()) {
            options.addArguments("--headless");
        }
        
        return options;
    }

    private SafariOptions getSafariOptions() {
        return new SafariOptions();
    }

    private void configureDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            // Configure timeouts
            if (testConfiguration != null) {
                TestConfiguration.UiConfig uiConfig = testConfiguration.getUi();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(uiConfig.getImplicitWait()));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(uiConfig.getPageLoadTimeout()));
            } else {
                // Fallback to default values
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.timeoutShort));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.timeoutLong));
            }
            
            driver.manage().deleteAllCookies(); // useful for AJAX pages
            
            if (!isHeadlessMode()) {
                driver.manage().window().maximize();
            }
            
            log.info("WebDriver configured successfully for browser: {}", applicationProperties.getBrowser());
        }
    }

    private void setupDriverWait() {
        driverWait.getDriverWaitThreadLocal()
                .set(new WebDriverWait(driverThreadLocal.get(), 
                    Duration.ofSeconds(Constants.timeoutShort), 
                    Duration.ofSeconds(Constants.pollingShort)));
    }

    private boolean isHeadlessMode() {
        if (testConfiguration != null) {
            return testConfiguration.getUi().isHeadless();
        }
        
        // Check system property, environment variable, or active profiles as fallback
        return Boolean.parseBoolean(System.getProperty("headless", "false")) ||
               Boolean.parseBoolean(System.getenv("HEADLESS")) ||
               Arrays.toString(this.environment.getActiveProfiles()).contains("headless");
    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    /**
     * Check if driver is existing and not null
     */
    public boolean isDriverExisting() {
        return getDriver() != null;
    }

    /**
     * Safely quit and clean up the driver
     */
    public void quitDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
                log.info("WebDriver quit successfully");
            } catch (Exception e) {
                log.warn("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                // Clean up temporary user data directory
                cleanupUserDataDirectory();
                
                // Remove ThreadLocal references
                driverThreadLocal.remove();
                userDataDirThreadLocal.remove();
                driverWait.getDriverWaitThreadLocal().remove();
            }
        }
    }
    
    /**
     * Clean up temporary Chrome user data directory
     */
    private void cleanupUserDataDirectory() {
        String userDataDir = userDataDirThreadLocal.get();
        if (userDataDir != null) {
            try {
                Path userDataPath = Paths.get(userDataDir);
                if (Files.exists(userDataPath)) {
                    deleteDirectoryRecursively(userDataPath);
                    log.debug("Cleaned up Chrome user data directory: {}", userDataDir);
                }
            } catch (Exception e) {
                log.warn("Failed to clean up Chrome user data directory {}: {}", userDataDir, e.getMessage());
            }
        }
    }
    
    /**
     * Recursively delete directory and its contents
     */
    private void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                .sorted((a, b) -> b.compareTo(a)) // Delete files first, then directories
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException e) {
                        log.warn("Failed to delete: {}", p.toString());
                    }
                });
        }
    }

    /**
     * Download driver binaries if needed (placeholder for future enhancement)
     */
    public void downloadDriver() {
        log.info("Driver download check - using WebDriverManager or system drivers");
        // Future enhancement: integrate with WebDriverManager for automatic driver downloads
    }
}
