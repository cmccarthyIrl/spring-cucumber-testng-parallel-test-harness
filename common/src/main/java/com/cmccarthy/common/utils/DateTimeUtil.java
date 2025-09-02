package com.cmccarthy.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static java.time.OffsetDateTime.now;

/**
 * Utility class for date and time operations used in test scenarios.
 * Provides various methods for date manipulation, formatting, and generation
 * of test-specific dates.
 */
@SuppressWarnings("unused")
public final class DateTimeUtil {

    private static final DateTimeFormatter ISO_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final DateTimeFormatter ISO_DATE_FORMAT_NO_TIME = DateTimeFormatter
            .ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter ISO_DATE_FORMAT_LONG_NO_TIME = DateTimeFormatter
            .ofPattern("d MMM yyyy");

    private static final DateTimeFormatter ISO_DATE_FORMAT_LONG_MILLISECONDS = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final DateTimeFormatter ISO_DATE_FORMAT_SHORT_NO_TIME = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DateTimeUtil() {
        throw new AssertionError("DateTimeUtil class should not be instantiated");
    }

    /**
     * Gets a fixed effective date for testing purposes.
     *
     * @return OffsetDateTime representing January 1, 2018
     */
    public static OffsetDateTime getEffectiveDate() {
        return getOffsetDateTime(2018, 1, 1);
    }

    /**
     * Gets a far future expiration date for testing purposes.
     *
     * @return OffsetDateTime representing January 1, 3000
     */
    public static OffsetDateTime getExpirationDate() {
        return getOffsetDateTime(3000, 1, 1);
    }

    /**
     * Gets the current Unix timestamp as a string.
     *
     * @return current Unix timestamp in seconds
     */
    public static String getNowUnixTimestampDate() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    /**
     * Creates an OffsetDateTime for the specified date at midnight UTC.
     *
     * @param year       the year
     * @param month      the month (1-12)
     * @param dayOfMonth the day of month (1-31)
     * @return OffsetDateTime at midnight UTC
     */
    public static OffsetDateTime getOffsetDateTime(int year, int month, int dayOfMonth) {
        return OffsetDateTime
                .of(LocalDate.of(year, month, dayOfMonth), LocalTime.of(0, 0), ZoneOffset.UTC);
    }

    /**
     * Gets the current local date as a formatted string.
     *
     * @return current date in dd/MM/yyyy format
     */
    public static String localDateNow() {
        return OffsetDateTime.of(now().toLocalDateTime(), ZoneOffset.UTC)
                .format(ISO_DATE_FORMAT_NO_TIME);
    }

    /**
     * Gets today's date as an OffsetDateTime.
     *
     * @return today's date in UTC
     */
    public static OffsetDateTime getDateToday() {
        return OffsetDateTime.of(now().toLocalDateTime(), ZoneOffset.UTC);
    }

    /**
     * Converts a date string to long date format.
     *
     * @param dateString date in dd/MM/yyyy format
     * @return date in long format (e.g., "1 Jan 2023")
     */
    public static String getLongDateStringFromDateString(String dateString) {
        LocalDate date = LocalDate.parse(dateString, ISO_DATE_FORMAT_NO_TIME);
        return ISO_DATE_FORMAT_LONG_NO_TIME.format(date);
    }

    /**
     * Converts a date string to short ISO format.
     *
     * @param dateString date in dd/MM/yyyy format
     * @return date in yyyy-MM-dd format
     */
    public static String getShortDateStringFromDateString(String dateString) {
        LocalDate date = LocalDate.parse(dateString, ISO_DATE_FORMAT_NO_TIME);
        return ISO_DATE_FORMAT_SHORT_NO_TIME.format(date);
    }

    /**
     * Gets the current date with milliseconds in ISO format.
     *
     * @return current date in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format
     */
    public static String getLongDateMillisecondsString() {
        return ISO_DATE_FORMAT_LONG_MILLISECONDS.format(getDateToday());
    }

    /**
     * Gets the current local date and time in UTC.
     *
     * @return current date and time as OffsetDateTime
     */
    public static OffsetDateTime localDateTimeNow() {
        return OffsetDateTime.of(now().toLocalDateTime(), ZoneOffset.UTC);
    }

    /**
     * Gets the current month and year as a formatted string.
     *
     * @return current month and year in MM/yyyy format
     */
    public static String getMonthYearNumericalString() {
        return String.format("%02d/%d", now().getMonthValue(), now().getYear());
    }

    /**
     * Gets the next weekday (Monday-Friday) in numerical format.
     *
     * @return next weekday in dd/MM/yyyy format
     */
    public static String getNextDayOfWeekNumericalFormat() {
        LocalDateTime date = LocalDateTime.now();
        do {
            date = date.plusDays(1);
        } while (date.getDayOfWeek().getValue() > 5); // 6=Saturday, 7=Sunday
        return date.format(ISO_DATE_FORMAT_NO_TIME);
    }

    /**
     * Manages feature date generation based on table input.
     * Expected format: "Day +/-", "Month +/-", "Year +/-" followed by number
     * Examples: "Day + 5", "Month - 2", "Year + 1"
     *
     * @param tableDate the table date specification
     * @return formatted date string or null if input is null
     * @throws IllegalArgumentException if the input format is invalid
     */
    public static String featureDateManager(String tableDate) {
        if (tableDate == null) {
            return null;
        }

        final String[] dateTableData = tableDate.trim().split("\\s+");
        final LocalDateTime date = LocalDateTime.now();

        if (dateTableData.length == 1 && "Day".equals(dateTableData[0])) {
            return date.format(ISO_DATE_FORMAT_NO_TIME);
        }

        if (dateTableData.length < 3) {
            throw new IllegalArgumentException("Invalid date format. Expected: 'Unit +/- Value' (e.g., 'Day + 5')");
        }

        final String unit = dateTableData[0];
        final String operation = dateTableData[1];
        final int dateValue;

        try {
            dateValue = Integer.parseInt(dateTableData[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value: " + dateTableData[2]);
        }

        final String switchType = unit + " " + operation;

        return switch (switchType) {
            case "Day +" -> date.plusDays(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
            case "Day -" -> date.minusDays(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
            case "Month +" -> date.plusMonths(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
            case "Month -" -> date.minusMonths(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
            case "Year +" -> date.plusYears(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
            case "Year -" -> date.minusYears(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
            default -> throw new IllegalArgumentException("Unsupported date operation: " + switchType + 
                ". Supported: Day +/-, Month +/-, Year +/-");
        };
    }
}
