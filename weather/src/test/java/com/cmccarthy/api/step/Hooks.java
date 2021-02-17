package com.cmccarthy.api.step;


import com.cmccarthy.common.utils.HookUtil;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private HookUtil hookUtil;

    @After
    public void afterScenario(Scenario scenario) {
        hookUtil.endOfTest(scenario);
    }
}