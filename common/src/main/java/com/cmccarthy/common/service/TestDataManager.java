package com.cmccarthy.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestDataManager {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Map<String, Object> testDataCache = new ConcurrentHashMap<>();
    private final ThreadLocal<Map<String, Object>> testContextData = new ThreadLocal<>();

    public void initializeTestContext() {
        testContextData.set(new HashMap<>());
    }

    public void clearTestContext() {
        testContextData.remove();
    }

    public void setContextValue(String key, Object value) {
        Map<String, Object> context = testContextData.get();
        if (context == null) {
            initializeTestContext();
            context = testContextData.get();
        }
        context.put(key, value);
    }

    public <T> T getContextValue(String key, Class<T> type) {
        Map<String, Object> context = testContextData.get();
        if (context == null) {
            return null;
        }
        Object value = context.get(key);
        return type.cast(value);
    }

    public Object getContextValue(String key) {
        Map<String, Object> context = testContextData.get();
        if (context == null) {
            return null;
        }
        return context.get(key);
    }

    public Map<String, Object> getAllContextValues() {
        Map<String, Object> context = testContextData.get();
        return context != null ? new HashMap<>(context) : new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> T loadTestData(String resourcePath, Class<T> type) {
        String cacheKey = resourcePath + "_" + type.getName();
        
        return (T) testDataCache.computeIfAbsent(cacheKey, key -> {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException("Resource not found: " + resourcePath);
                }
                
                return jsonMapper.readValue(inputStream, type);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load test data from: " + resourcePath, e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> loadTestDataAsMap(String resourcePath) {
        return loadTestData(resourcePath, Map.class);
    }

    public void clearCache() {
        testDataCache.clear();
    }

    public String generateUniqueId() {
        return "test_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId();
    }

    public String generateTestEmail() {
        return "test_" + System.currentTimeMillis() + "@example.com";
    }

    public String generateTestUsername() {
        return "user_" + System.currentTimeMillis();
    }
}
