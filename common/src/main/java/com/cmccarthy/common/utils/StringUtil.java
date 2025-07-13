package com.cmccarthy.common.utils;

import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class StringUtil {

    private static final Random random = new Random();
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String ALPHANUMERIC = ALPHABET + NUMBERS;

    /**
     * Generates a random string from the given character set.
     *
     * @param length  the length of the string to generate
     * @param charset the character set to use
     * @return a random string
     */
    private static String generateRandomString(int length, String charset) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(charset.charAt(secureRandom.nextInt(charset.length())));
        }
        return sb.toString();
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static int getRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    "Min (" + min + ") should not be larger than Max (" + max + ")");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static String getRandomAlphaString(int length) {
        return generateRandomString(length, ALPHABET);
    }

    public static String getRandomAlphaString(int min, int max) {
        return generateRandomString(getRandomNumber(min, max), ALPHABET);
    }

    public static String getRandomNumericString(int length, int min, int max) {
        // Generate numeric string with values between min and max
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = ThreadLocalRandom.current().nextInt(min, max + 1) % 10;
            sb.append(digit);
        }
        return sb.toString();
    }

    public static String getRandomNumericString(int min, int max) {
        return generateRandomString(getRandomNumber(min, max), NUMBERS);
    }

    public static String getRandomAlphaNumericString(int length) {
        return generateRandomString(length, ALPHANUMERIC);
    }

    public static String getRandomAlphaNumericString(int min, int max) {
        return generateRandomString(getRandomNumber(min, max), ALPHANUMERIC);
    }

    public static String getRandomAmount(String min, String max) {
        if (Double.parseDouble(min) > Double.parseDouble(max)) {
            throw new IllegalArgumentException(
                    ": Min (" + min + ") should not be larger than Max (" + max + ")");
        }
        return String.format("%.2f", ThreadLocalRandom.current()
                .nextDouble(Double.parseDouble(min), Double.parseDouble(max) + 0.01));
    }

    public static double getRandomAmount(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    ": Min (" + min + ") should not be larger than Max (" + max + ")");
        }
        return Double.parseDouble(
                String.format("%.2f", ThreadLocalRandom.current().nextDouble(min, max + 0.01)));
    }

    public static String featureStringManager(String tableValue) {
        if (tableValue != null) {
            final String[] dateTableData = tableValue.split("\\s+");

            final String switchType = dateTableData[0];

            final int length = Integer.parseInt(dateTableData[1]);

            switch (switchType) {
                case "StringInteger":
                    return getRandomAlphaNumericString(length);
                case "String":
                    return getRandomAlphaString(length);
                case "Integer":
                    return getRandomNumericString(length, 2, 66);
                default:
                    throw new NoSuchElementException("Could not create a String of type : " + switchType);
            }
        } else {
            return null;
        }
    }
}
