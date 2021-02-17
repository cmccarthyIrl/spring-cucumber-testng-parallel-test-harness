package com.cmccarthy.ui.step;

import com.cmccarthy.common.utils.HookUtil;
import com.cmccarthy.ui.config.WikipediaAbstractTestDefinition;
import com.cmccarthy.ui.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;


public class Hooks extends WikipediaAbstractTestDefinition {

    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) {
        driverManager.driverManager();
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
        driverManager.getDriver().quit();
    }
}