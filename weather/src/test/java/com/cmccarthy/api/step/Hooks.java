package com.cmccarthy.api.step;


import com.cmccarthy.api.config.WeatherAbstractTestDefinition;
import com.cmccarthy.common.utils.HookUtil;
import com.cmccarthy.common.utils.LogManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

@CucumberContextConfiguration
public class Hooks extends WeatherAbstractTestDefinition {
  @Autowired
  private LogManager logManager;
  @Autowired
  private HookUtil hookUtil;

  @Before
  public void beforeScenario(Scenario scenario) {
    String filename = scenario.getName().replaceAll("\\s+", "_");
    logManager.createNewLogger(filename);
  }

  @After
  public void afterScenario(Scenario scenario) {
    hookUtil.endOfTest(scenario);
  }
}