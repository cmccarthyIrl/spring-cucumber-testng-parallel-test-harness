package com.cmccarthy.common.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class StepDefinitionDataManager {

  private final ThreadLocal<Map<String, Object>> storedObjectMap = new ThreadLocal<>();

  public Map<String, Object> getStoredObjectMap() {
    return storedObjectMap.get();
  }

  public void addToStoredObjectMap(String value, Object object) {
    final Map<String, Object> tempObjectMap;
    if (getStoredObjectMap() == null) {
      tempObjectMap = new HashMap<>();
    } else {
      tempObjectMap = getStoredObjectMap();
      tempObjectMap.remove(value);
    }
    tempObjectMap.put(value, object);
    storedObjectMap.set(tempObjectMap);
  }
}

