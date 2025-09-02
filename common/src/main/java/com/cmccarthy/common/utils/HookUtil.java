package com.cmccarthy.common.utils;

import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Utility service for handling test hooks and post-test cleanup operations.
 * This service manages test scenario lifecycle events and logging.
 */
@Service
public class HookUtil {

    private static final String LOG_SEPARATOR = "==========================================================================";
    private static final String TEST_STATUS_PREFIX = "================================Test ";
    private static final String TEST_STATUS_SUFFIX = "===============================";
    
    @Autowired
    private LogManager logManager;

    /**
     * Handles end-of-test operations including screenshot attachment for failed scenarios
     * and logging of test results.
     *
     * @param scenario the completed test scenario
     */
    public void endOfTest(Scenario scenario) {
        if (scenario.getStatus() != null && scenario.isFailed()) {
            attachScreenshotForFailedScenario(scenario);
        }

        logTestResult(scenario);
    }

    /**
     * Attaches a screenshot for failed scenarios.
     *
     * @param scenario the failed test scenario
     */
    private void attachScreenshotForFailedScenario(Scenario scenario) {
        String sanitizedScenarioName = scenario.getName().replaceAll("\\s+", "_");
        String sanitizedFeatureName = scenario.getId()
                .replaceAll("\\s+", "_")
                .replaceAll(":", "_")
                .split("\\.")[1];
        String filename = sanitizedScenarioName + "_" + sanitizedFeatureName;
        
        scenario.attach(filename.getBytes(StandardCharsets.UTF_8), "image/png", filename);
    }

    /**
     * Logs the test result with formatted output.
     *
     * @param scenario the completed test scenario
     */
    private void logTestResult(Scenario scenario) {
        logManager.info("");
        logManager.info(LOG_SEPARATOR);
        logManager.info(TEST_STATUS_PREFIX + scenario.getStatus() + TEST_STATUS_SUFFIX);
        logManager.info(LOG_SEPARATOR);
        logManager.info("");
    }
}