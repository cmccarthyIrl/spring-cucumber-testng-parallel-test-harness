package com.cmccarthy.ui.utils;

import com.cmccarthy.common.utils.ApplicationProperties;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Capabilities;
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

import java.net.URL;
import java.util.NoSuchElementException;

import org.openqa.selenium.WebDriver;

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
//            WebDriverRunner.getWebDriver().manage().deleteAllCookies();
        }
    }

    public void setLocalWebDriver() {
        switch (applicationProperties.getBrowser()) {
            case ("chrome"):
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                driverThreadLocal.set(new ChromeDriver());
                break;
            case ("firefox"):
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/../common/src/main/resources/geckodriver");
                FirefoxOptions firefoxOptions = new FirefoxOptions();
//                firefoxOptions.setHeadless(true);
                firefoxOptions.setCapability("marionette", true);
                webDriver = new FirefoxDriver(firefoxOptions);
                driverThreadLocal.set(webDriver);
                break;
            case ("ie"):
                System.setProperty("webdriver.ie.driver", "IEDriverServer.exe");
                DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
                capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                driverThreadLocal.set(new InternetExplorerDriver(capabilitiesIE));
                break;
            case ("safari"):
                System.setProperty("webdriver.opera.driver", "operadriver.exe");
                driverThreadLocal.set(new OperaDriver());
                break;
            case ("edge"):
                System.setProperty("webdriver.edge.driver", "MicrosoftWebDriver.exe");
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
}
