package com.cmccarthy.ui.utils;

import com.cmccarthy.common.config.TestConfiguration;
import com.cmccarthy.common.utils.ApplicationProperties;
import com.cmccarthy.common.utils.Constants;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
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
import java.time.Duration;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Component
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private final Logger log = LoggerFactory.getLogger(DriverManager.class);

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private TestConfiguration testConfig;
    @Autowired
    private DriverWait driverWait;
    @Autowired
    private Environment environment;

    public void createDriver() throws IOException {
        if (getDriver() == null) {
            if (Arrays.toString(this.environment.getActiveProfiles()).contains("cloud-provider")) {
                setRemoteDriver(new URL(applicationProperties.getGridUrl()));
            } else {
                setLocalWebDriver();
            }
            getDriver().manage().deleteAllCookies();//useful for AJAX pages
            
            // Set the window size if specified in the configuration
            if (testConfig.getUi().getWindowSize() != null && !testConfig.getUi().getWindowSize().isEmpty()) {
                String[] dimensions = testConfig.getUi().getWindowSize().split("x");
                if (dimensions.length == 2) {
                    try {
                        int width = Integer.parseInt(dimensions[0]);
                        int height = Integer.parseInt(dimensions[1]);
                        getDriver().manage().window().setSize(new Dimension(width, height));
                        log.info("Set browser window size to: {}x{}", width, height);
                    } catch (NumberFormatException e) {
                        log.error("Failed to parse window size from configuration: {}", testConfig.getUi().getWindowSize());
                    }
                }
            }
        }
    }

    public void setLocalWebDriver() throws IOException {
        String browser = applicationProperties.getBrowser();
        boolean isHeadless = testConfig.getUi().isHeadless();
        
        switch (browser) {
            case ("chrome") -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-logging");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--start-maximized");
                if (isHeadless) {
                    options.addArguments("--headless=new");
                }
                driverThreadLocal.set(new ChromeDriver(options));
            }
            case ("firefox") -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("marionette", true);
                if (isHeadless) {
                    firefoxOptions.addArguments("--headless");
                }
                driverThreadLocal.set(new FirefoxDriver(firefoxOptions));
            }
            case ("ie") -> {
                InternetExplorerOptions capabilitiesIE = new InternetExplorerOptions();
                capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                driverThreadLocal.set(new InternetExplorerDriver(capabilitiesIE));
            }
            case ("safari") -> {
                SafariOptions operaOptions = new SafariOptions();
                driverThreadLocal.set(new SafariDriver(operaOptions));
            }
            case ("edge") -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    edgeOptions.addArguments("--headless");
                }
                driverThreadLocal.set(new EdgeDriver(edgeOptions));
            }
            default ->
                    throw new NoSuchElementException("Failed to create an instance of WebDriver for: " + browser);
        }
        
        // Configure WebDriver timeouts
        WebDriver driver = driverThreadLocal.get();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(testConfig.getUi().getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(testConfig.getUi().getPageLoadTimeout()));
        
        driverWait.getDriverWaitThreadLocal()
                .set(new WebDriverWait(driver, 
                        Duration.ofSeconds(testConfig.getTimeoutSeconds()), 
                        Duration.ofMillis(Constants.pollingShort)));
    }

    private void setRemoteDriver(URL hubUrl) {
        Capabilities capability;
        String browser = applicationProperties.getBrowser();
        boolean isHeadless = testConfig.getUi().isHeadless();
        
        switch (browser) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (isHeadless) {
                    options.addArguments("--headless");
                }
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, options));
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                if (isHeadless) {
                    options.addArguments("--headless");
                }
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, options));
            }
            case "ie" -> {
                capability = new InternetExplorerOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            case "safari" -> {
                capability = new SafariOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            case ("edge") -> {
                EdgeOptions options = new EdgeOptions();
                if (isHeadless) {
                    options.addArguments("--headless");
                }
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, options));
            }
            default ->
                    throw new NoSuchElementException("Failed to create an instance of RemoteWebDriver for: " + browser);
        }
        
        // Configure WebDriver timeouts
        WebDriver driver = driverThreadLocal.get();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(testConfig.getUi().getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(testConfig.getUi().getPageLoadTimeout()));
        
        driverWait.getDriverWaitThreadLocal()
                .set(new WebDriverWait(driver, 
                        Duration.ofSeconds(testConfig.getTimeoutSeconds()), 
                        Duration.ofMillis(Constants.pollingShort)));
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
                // Remove ThreadLocal references
                driverThreadLocal.remove();
                driverWait.getDriverWaitThreadLocal().remove();
            }
        }
    }
}