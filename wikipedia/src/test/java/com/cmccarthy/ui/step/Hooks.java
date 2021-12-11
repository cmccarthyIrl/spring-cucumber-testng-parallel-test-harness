package com.cmccarthy.ui.step;

import com.cmccarthy.common.utils.HookUtil;
import com.cmccarthy.ui.config.WikipediaAbstractTestDefinition;
import com.cmccarthy.ui.utils.DriverManager;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.IAlterSuiteListener;
import org.testng.IAnnotationTransformer;
import org.testng.IConfigurationListener2;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.ITestListener;


//public class Hooks extends WikipediaAbstractTestDefinition implements IConfigurationListener2, IExecutionListener, IAlterSuiteListener, IAnnotationTransformer, IInvokedMethodListener, ITestListener, IReporter {
public class Hooks extends WikipediaAbstractTestDefinition {
    @Autowired
    private HookUtil hookUtil;
    @Autowired
    private DriverManager driverManager;

    @Before
    public void beforeScenario(Scenario scenario) {
        driverManager.createDriver();
//        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
        WebDriverRunner.closeWebDriver();
    }
}