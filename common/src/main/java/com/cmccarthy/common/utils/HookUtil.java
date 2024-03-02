package com.cmccarthy.common.utils;

import io.cucumber.java.Scenario;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HookUtil {

  @Autowired
  LogManager logManager;

  public void endOfTest(Scenario scenario) {

    if (scenario.getStatus() != null) {
      if (scenario.isFailed()) {
        String filename = scenario.getName().replaceAll("\\s+", "_");
        final String featureError = scenario.getId().replaceAll("\\s+", "_").replaceAll(":", "_").split("\\.")[1];
        filename = filename + "_" + featureError;
        scenario.attach(filename.getBytes(StandardCharsets.UTF_8), "image/png", filename);
      }
    }

    logManager.info("");
    logManager.info("==========================================================================");
    logManager.info("================================Test " + scenario.getStatus().toString() + "===============================");
    logManager.info("==========================================================================");
    logManager.info("");
  }


}