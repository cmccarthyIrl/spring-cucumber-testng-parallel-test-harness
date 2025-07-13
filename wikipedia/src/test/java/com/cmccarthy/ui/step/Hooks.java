package com.cmccarthy.ui.step;

import com.cmccarthy.common.utils.HookUtil;
import com.cmccarthy.common.utils.LogManager;
import com.cmccarthy.ui.config.WikipediaAbstractTestDefinition;
import com.cmccarthy.ui.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@CucumberContextConfiguration
public class Hooks extends WikipediaAbstractTestDefinition {
    
    @Autowired
    private LogManager logManager;
    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) throws IOException {
        String filename = scenario.getName().replaceAll("\\s+", "_");
        logManager.createNewLogger(filename);
        driverManager.createDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
        driverManager.quitDriver();
    }
}