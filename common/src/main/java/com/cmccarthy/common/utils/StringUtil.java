package com.cmccarthy.common.utils;

import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating random strings and numbers for test data.
 * This class provides various methods to generate random data that can be used
 * in test scenarios for creating test inputs.
 */
@SuppressWarnings("unused")
public final class StringUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String ALPHANUMERIC = ALPHABET + NUMBERS;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private StringUtil() {
        throw new AssertionError("StringUtil class should not be instantiated");
    }

    /**
     * Generates a random string from the given character set.
     *
     * @param length  the length of the string to generate
     * @param charset the character set to use
     * @return a random string
     * @throws IllegalArgumentException if length is negative
     */
    private static String generateRandomString(int length, String charset) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative, but was: " + length);
        }
        if (length == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(charset.charAt(SECURE_RANDOM.nextInt(charset.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random boolean value.
     *
     * @return a random boolean
     */
    public static boolean getRandomBoolean() {
        return SECURE_RANDOM.nextBoolean();
    }

    /**
     * Generates a random integer between min and max (inclusive).
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return a random integer
     * @throws IllegalArgumentException if min is greater than max
     */
    public static int getRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    "Min (" + min + ") should not be greater than Max (" + max + ")");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Generates a random alphabetic string of specified length.
     *
     * @param length the length of the string to generate
     * @return a random alphabetic string
     */
    public static String getRandomAlphaString(int length) {
        return generateRandomString(length, ALPHABET);
    }

    /**
     * Generates a random alphabetic string of random length between min and max.
     *
     * @param min the minimum length
     * @param max the maximum length  
     * @return a random alphabetic string
     */
    public static String getRandomAlphaString(int min, int max) {
        return generateRandomString(getRandomNumber(min, max), ALPHABET);
    }

    /**
     * Generates a random numeric string of specified length.
     *
     * @param length the length of the string to generate
     * @return a random numeric string
     */
    public static String getRandomNumericString(int length) {
        return generateRandomString(length, NUMBERS);
    }

    /**
     * Generates a random numeric string of random length between min and max.
     *
     * @param min the minimum length
     * @param max the maximum length
     * @return a random numeric string
     */
    public static String getRandomNumericString(int min, int max) {
        return generateRandomString(getRandomNumber(min, max), NUMBERS);
    }

    /**
     * Generates a random alphanumeric string of specified length.
     *
     * @param length the length of the string to generate
     * @return a random alphanumeric string
     */
    public static String getRandomAlphaNumericString(int length) {
        return generateRandomString(length, ALPHANUMERIC);
    }

    /**
     * Generates a random alphanumeric string of random length between min and max.
     *
     * @param min the minimum length
     * @param max the maximum length
     * @return a random alphanumeric string
     */
    public static String getRandomAlphaNumericString(int min, int max) {
        return generateRandomString(getRandomNumber(min, max), ALPHANUMERIC);
    }

    /**
     * Generates a random amount as a string between min and max values.
     *
     * @param min the minimum value as string
     * @param max the maximum value as string
     * @return a random amount formatted to 2 decimal places
     * @throws IllegalArgumentException if min is greater than max
     */
    public static String getRandomAmount(String min, String max) {
        double minValue = Double.parseDouble(min);
        double maxValue = Double.parseDouble(max);
        
        if (minValue > maxValue) {
            throw new IllegalArgumentException(
                    "Min (" + min + ") should not be greater than Max (" + max + ")");
        }
        return String.format("%.2f", ThreadLocalRandom.current()
                .nextDouble(minValue, maxValue + 0.01));
    }

    /**
     * Generates a random amount as a double between min and max values.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a random amount formatted to 2 decimal places
     * @throws IllegalArgumentException if min is greater than max
     */
    public static double getRandomAmount(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    "Min (" + min + ") should not be greater than Max (" + max + ")");
        }
        return Double.parseDouble(
                String.format("%.2f", ThreadLocalRandom.current().nextDouble(min, max + 0.01)));
    }

    /**
     * Manages feature string generation based on table value input.
     * Expected format: "StringType Length" (e.g., "String 10", "Integer 5")
     *
     * @param tableValue the table value containing type and length
     * @return a random string based on the specified type and length
     * @throws NoSuchElementException if the string type is not supported
     */
    public static String featureStringManager(String tableValue) {
        if (tableValue == null) {
            return null;
        }
        
        final String[] tableData = tableValue.trim().split("\\s+");
        if (tableData.length < 2) {
            throw new IllegalArgumentException("Invalid table value format. Expected: 'Type Length'");
        }

        final String stringType = tableData[0];
        final int length;
        
        try {
            length = Integer.parseInt(tableData[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid length value: " + tableData[1]);
        }

        return switch (stringType) {
            case "StringInteger" -> getRandomAlphaNumericString(length);
            case "String" -> getRandomAlphaString(length);
            case "Integer" -> getRandomNumericString(length);
            default -> throw new NoSuchElementException("Unsupported string type: " + stringType + 
                ". Supported types: String, Integer, StringInteger");
        };
    }
}
