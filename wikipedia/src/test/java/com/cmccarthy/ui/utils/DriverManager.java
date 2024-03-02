package com.cmccarthy.ui.utils;

import com.cmccarthy.common.utils.ApplicationProperties;
import com.cmccarthy.common.utils.Constants;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private final Logger log = LoggerFactory.getLogger(DriverManager.class);

    @Autowired
    private ApplicationProperties applicationProperties;
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
            WebDriverRunner.setWebDriver(getDriver());
            WebDriverRunner.getWebDriver().manage().deleteAllCookies();//useful for AJAX pages
        }
    }

    public void setLocalWebDriver() throws IOException {
        switch (applicationProperties.getBrowser()) {
            case ("chrome") -> {
                String path = Arrays.toString(this.environment.getActiveProfiles()).contains("headless-github") ?
                        System.getProperty("user.dir") + "/src/test/resources/drivers" : Constants.DRIVER_DIRECTORY;
                ChromeDriverService src = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(path + "/chromedriver" + getExtension()))
                        .usingAnyFreePort().build();
                src.start();

                System.setProperty("webdriver.chrome.driver", path + "/chromedriver" + getExtension());

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-logging");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--headless=new");
                driverThreadLocal.set(new ChromeDriver(src, options));
            }
            case ("firefox") -> {
                System.setProperty("webdriver.gecko.driver", Constants.DRIVER_DIRECTORY + "/geckodriver" + getExtension());
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
                firefoxOptions.setCapability("marionette", true);
                driverThreadLocal.set(new FirefoxDriver(firefoxOptions));
            }
            case ("ie") -> {
                System.setProperty("webdriver.ie.driver", Constants.DRIVER_DIRECTORY + "/IEDriverServer" + getExtension());
                InternetExplorerOptions capabilitiesIE = new InternetExplorerOptions();
                capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                driverThreadLocal.set(new InternetExplorerDriver(capabilitiesIE));
            }
            case ("safari") -> {
                SafariOptions operaOptions = new SafariOptions();
                System.setProperty("webdriver.opera.driver", Constants.DRIVER_DIRECTORY + "/operadriver" + getExtension());
                driverThreadLocal.set(new SafariDriver(operaOptions));
            }
            case ("edge") -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                System.setProperty("webdriver.edge.driver", Constants.DRIVER_DIRECTORY + "/MicrosoftWebDriver" + getExtension());
                driverThreadLocal.set(new EdgeDriver(edgeOptions));
            }
            default ->
                    throw new NoSuchElementException("Failed to create an instance of WebDriver for: " + applicationProperties.getBrowser());
        }
        driverWait.getDriverWaitThreadLocal()
                .set(new WebDriverWait(driverThreadLocal.get(), Duration.ofSeconds(Constants.timeoutShort), Duration.ofSeconds(Constants.pollingShort)));
    }

    private void setRemoteDriver(URL hubUrl) {
        Capabilities capability;
        switch (applicationProperties.getBrowser()) {
            case "firefox" -> {
                capability = new FirefoxOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--headless");
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
                capability = new EdgeOptions();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
            }
            default ->
                    throw new NoSuchElementException("Failed to create an instance of RemoteWebDriver for: " + applicationProperties.getBrowser());
        }
        driverWait.getDriverWaitThreadLocal()
                .set(new WebDriverWait(driverThreadLocal.get(), Duration.ofSeconds(Constants.timeoutShort), Duration.ofSeconds(Constants.pollingShort)));
    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    public boolean isDriverExisting() {
        File geckoDriver = new File(Constants.DRIVER_DIRECTORY + "/geckodriver" + getExtension());
        File chromedriver = new File(Constants.DRIVER_DIRECTORY + "/chromedriver" + getExtension());
        return geckoDriver.exists() && chromedriver.exists();
    }

    public void downloadDriver() {
        if (!Arrays.toString(this.environment.getActiveProfiles()).contains("headless-github")) {
            try {
                Process process;
                if (getOperatingSystem().equals("win")) {
                    process = Runtime.getRuntime().exec("cmd.exe /c downloadDriver.sh", null,
                            new File(Constants.COMMON_RESOURCES));
                } else if (getOperatingSystem().equals("linux")) {
                    process = Runtime.getRuntime().exec(
                            new String[]{"sh", "-c", Constants.COMMON_RESOURCES + "/downloadDriver.sh"});
                } else {
                    process = Runtime.getRuntime().exec(new String[]{"/bin/sh -c ls", Constants.COMMON_RESOURCES + "/downloadDriver.sh"});
                }
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    log.debug(line);
                    line = reader.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getOperatingSystem() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ROOT);
        if (os.contains("mac") || os.contains("darwin")) {
            return "mac";
        } else if (os.contains("win")) {
            return "win";
        } else {
            return "linux";
        }
    }

    private String getExtension() {
        String extension = "";
        if (getOperatingSystem().contains("win")) {
            return ".exe";
        }
        return extension;
    }
}
