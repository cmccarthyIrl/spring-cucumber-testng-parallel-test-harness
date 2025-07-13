package com.cmccarthy.common.listeners;

import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestExecutionListener implements IExecutionListener, ITestListener {

    @Override
    public void onExecutionStart() {
        System.out.println("========================================");
        System.out.println("Starting Test Execution");
        System.out.println("Thread Count: " + Thread.activeCount());
        System.out.println("========================================");
    }

    @Override
    public void onExecutionFinish() {
        System.out.println("========================================");
        System.out.println("Test Execution Completed");
        System.out.println("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        System.out.printf("[%s] Starting test: %s.%s%n", 
            Thread.currentThread().getName(), className, testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.printf("[%s] ✅ PASSED: %s (%dms)%n", 
            Thread.currentThread().getName(), testName, duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();
        System.out.printf("[%s] ❌ FAILED: %s (%dms) - %s%n", 
            Thread.currentThread().getName(), testName, duration, 
            result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.printf("[%s] ⏭️ SKIPPED: %s%n", 
            Thread.currentThread().getName(), testName);
    }
}
