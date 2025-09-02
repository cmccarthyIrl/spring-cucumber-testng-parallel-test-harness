package com.cmccarthy.common.utils;

/**
 * Constants class containing timeout and polling values used throughout the test framework.
 */
public final class Constants {

    /**
     * Long timeout duration in seconds for extended wait operations.
     */
    public static final long TIMEOUT_LONG = 30;

    /**
     * Long polling interval in milliseconds for extended wait operations.
     */
    public static final long POLLING_LONG = 200;

    /**
     * Short timeout duration in seconds for quick wait operations.
     */
    public static final long TIMEOUT_SHORT = 10;

    /**
     * Short polling interval in milliseconds for quick wait operations.
     */
    public static final long POLLING_SHORT = 100;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Constants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
}
