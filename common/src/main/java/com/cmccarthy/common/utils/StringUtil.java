package com.cmccarthy.common.utils;

import static org.apache.commons.lang3.RandomStringUtils.random;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class StringUtil {

  private static final Random random = new Random();

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
    return random(length, true, false);
  }

  public static String getRandomAlphaString(int min, int max) {
    return random(getRandomNumber(min, max), true, false);
  }

  public static String getRandomNumericString(int length, int min, int max) {
    return random(length, min, max, false, true);
  }

  public static String getRandomNumericString(int min, int max) {
    return random(getRandomNumber(min, max), false, true);
  }

  public static String getRandomAlphaNumericString(int length) {
    return random(length, true, true);
  }

  public static String getRandomAlphaNumericString(int min, int max) {
    return random(getRandomNumber(min, max), true, true);
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
