package com.cmccarthy.common.utils;

import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class HookUtil {

    private final Logger log = LoggerFactory.getLogger(HookUtil.class);

    public void endOfTest(Scenario scenario) {

        if (scenario.getStatus() != null) {
            if (scenario.isFailed()) {
                String filename = scenario.getName().replaceAll("\\s+", "_");
                final String featureError = scenario.getId().replaceAll("\\s+", "_").replaceAll(":", "_").split("\\.")[1];
                filename = filename + "_" + featureError;
                scenario.attach(filename.getBytes(StandardCharsets.UTF_8), "image/png", filename);
            }
        }

        log.info("");
        log.info("==========================================================================");
        log.info("================================Test " + scenario.getStatus().toString() + "===============================");
        log.info("==========================================================================");
        log.info("");
    }
}