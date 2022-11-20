package com.cmccarthy.ui.step;

import com.cmccarthy.common.utils.HookUtil;
import com.cmccarthy.common.utils.LogManager;
import com.cmccarthy.ui.config.WikipediaAbstractTestDefinition;
import com.cmccarthy.ui.utils.DriverManager;
import com.codeborne.selenide.WebDriverRunner;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;

@CucumberContextConfiguration
public class Hooks extends WikipediaAbstractTestDefinition {
    @Autowired
    private LogManager logManager;
    private static final Object lock = new Object();
    private static boolean initialized = false;
    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) throws MalformedURLException {
        String filename = scenario.getName().replaceAll("\\s+", "_");
        logManager.createNewLogger(filename);

        synchronized (lock) {
            if (!initialized) {
                if (!driverManager.isDriverExisting()) {
                    driverManager.downloadDriver();
                }
                initialized = true;
            }
        }
        driverManager.createDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
        if (driverManager.getDriver() != null) {
            WebDriverRunner.closeWebDriver();
        }
    }
}