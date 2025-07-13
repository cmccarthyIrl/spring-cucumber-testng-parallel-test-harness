package com.cmccarthy.common.exception;

public class TestFrameworkException extends RuntimeException {
    
    private final String testName;
    private final String errorType;
    
    public TestFrameworkException(String message) {
        super(message);
        this.testName = null;
        this.errorType = "GENERAL";
    }
    
    public TestFrameworkException(String message, Throwable cause) {
        super(message, cause);
        this.testName = null;
        this.errorType = "GENERAL";
    }
    
    public TestFrameworkException(String message, String testName, String errorType) {
        super(message);
        this.testName = testName;
        this.errorType = errorType;
    }
    
    public TestFrameworkException(String message, Throwable cause, String testName, String errorType) {
        super(message, cause);
        this.testName = testName;
        this.errorType = errorType;
    }
    
    public String getTestName() {
        return testName;
    }
    
    public String getErrorType() {
        return errorType;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestFrameworkException: ");
        if (testName != null) {
            sb.append("[Test: ").append(testName).append("] ");
        }
        if (errorType != null) {
            sb.append("[Type: ").append(errorType).append("] ");
        }
        sb.append(getMessage());
        return sb.toString();
    }
}
