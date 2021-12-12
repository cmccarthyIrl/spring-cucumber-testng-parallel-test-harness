package com.cmccarthy.ui.utils;

import com.cmccarthy.common.utils.ApplicationProperties;
import com.cmccarthy.common.utils.Constants;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Wait<WebDriver>> driverWaitThreadLocal = new ThreadLocal<>();
    private static WebDriver webDriver;
    @Autowired
    private ApplicationProperties applicationProperties;

    public void createDriver() {
        if (getDriver() == null) {
            setLocalWebDriver();
            WebDriverRunner.setWebDriver(getDriver());
            WebDriverRunner.getWebDriver().manage().deleteAllCookies();//useful for AJAX pages
        }
    }

    public void setLocalWebDriver() {
        switch (applicationProperties.getBrowser()) {
            case ("chrome"):
                System.setProperty("webdriver.chrome.driver", Constants.DRIVER_DIRECTORY + "/chromedriver" + getOSExtension());
                driverThreadLocal.set(new ChromeDriver());
                break;
            case ("firefox"):
                System.setProperty("webdriver.gecko.driver", Constants.DRIVER_DIRECTORY + "/geckodriver" + getOSExtension());
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("marionette", true);
                webDriver = new FirefoxDriver(firefoxOptions);
                driverThreadLocal.set(webDriver);
                break;
            case ("ie"):
                System.setProperty("webdriver.ie.driver", Constants.DRIVER_DIRECTORY + "/IEDriverServer" + getOSExtension());
                DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
                capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                driverThreadLocal.set(new InternetExplorerDriver(capabilitiesIE));
                break;
            case ("safari"):
                System.setProperty("webdriver.opera.driver", Constants.DRIVER_DIRECTORY + "/operadriver" + getOSExtension());
                driverThreadLocal.set(new OperaDriver());
                break;
            case ("edge"):
                System.setProperty("webdriver.edge.driver", Constants.DRIVER_DIRECTORY + "/MicrosoftWebDriver" + getOSExtension());
                driverThreadLocal.set(new EdgeDriver());
                break;
            default:
                throw new NoSuchElementException("Failed to create an instance of WebDriver for: " + applicationProperties.getBrowser());
        }
        driverWaitThreadLocal.set(new WebDriverWait(driverThreadLocal.get(), 10, 500));

    }

    private void setRemoteDriver(URL hubUrl) {
        Capabilities capability;
        switch (applicationProperties.getBrowser()) {
            case "firefox":
                capability = DesiredCapabilities.firefox();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case "chrome":
                capability = DesiredCapabilities.chrome();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case "ie":
                capability = DesiredCapabilities.internetExplorer();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case "safari":
                capability = DesiredCapabilities.safari();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            case ("edge"):
                capability = DesiredCapabilities.edge();
                driverThreadLocal.set(new RemoteWebDriver(hubUrl, capability));
                break;
            default:
                throw new NoSuchElementException("Failed to create an instance of RemoteWebDriver for: " + applicationProperties.getBrowser());
        }
        driverWaitThreadLocal.set(new WebDriverWait(driverThreadLocal.get(), 10, 500));
    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public Wait<WebDriver> getDriverWait() {
        return driverWaitThreadLocal.get();
    }

    public boolean checkIfDriverExists() {
        File geckoDriver = new File(Constants.DRIVER_DIRECTORY + "/geckodriver" + getOSExtension());
        File chromedriver = new File(Constants.DRIVER_DIRECTORY + "/geckodriver" + getOSExtension());
        return geckoDriver.exists() && chromedriver.exists();
    }

    public void downloadDriver() {
        System.out.println(" inside download driver ");
        try {
            Process process;
            if (getOperatingSystem().equals("win")) {
                process = Runtime.getRuntime().exec("cmd.exe /c downloadDriver.sh", null,
                        new File(Constants.COMMON_RESOURCES));
            } else {
                String filePath = Constants.COMMON_RESOURCES+"/downloadDriver.sh";
                process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", filePath}, null);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
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

    private String getOSExtension() {
        String extension = "";
        if (getOperatingSystem().contains("win")) {
            return ".exe.";
        }
        return extension;
    }
}
