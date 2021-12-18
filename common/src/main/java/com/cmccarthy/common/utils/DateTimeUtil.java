package com.cmccarthy.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static java.time.OffsetDateTime.now;

@SuppressWarnings("unused")
public class DateTimeUtil {

    private static final DateTimeFormatter ISO_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final DateTimeFormatter ISO_DATE_FORMAT_NO_TIME = DateTimeFormatter
            .ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter ISO_DATE_FORMAT_LONG_NO_TIME = DateTimeFormatter
            .ofPattern("d MMM yyyy");

    private static final DateTimeFormatter ISO_DATE_FORMAT_LONG_MILI = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final DateTimeFormatter ISO_DATE_FORMAT_SHORT_NO_TIME = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    public static OffsetDateTime getEffectiveDate() {
        return getOffsetDateTime(2018, 1, 1);
    }

    public static OffsetDateTime getExpirationDate() {
        return getOffsetDateTime(3000, 1, 1);
    }

    public static String getNowUnixTimestampDate() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static OffsetDateTime getOffsetDateTime(int year, int month, int dayOfMonth) {
        return OffsetDateTime
                .of(LocalDate.of(year, month, dayOfMonth), LocalTime.of(0, 0), ZoneOffset.UTC);
    }

    public static String localDateNow() {
        return OffsetDateTime.of(now().toLocalDateTime(), ZoneOffset.UTC)
                .format(ISO_DATE_FORMAT_NO_TIME);
    }

    public static OffsetDateTime getDateToday() {
        return OffsetDateTime.of(now().toLocalDateTime(), ZoneOffset.UTC);
    }

    public static String getLongDateStringFromDateString(String dateString) {
        LocalDate date = LocalDate.parse(dateString, ISO_DATE_FORMAT_NO_TIME);
        return ISO_DATE_FORMAT_LONG_NO_TIME.format(date);
    }

    public static String getShortDateStringFromDateString(String dateString) {
        LocalDate date = LocalDate.parse(dateString, ISO_DATE_FORMAT_NO_TIME);
        return ISO_DATE_FORMAT_SHORT_NO_TIME.format(date);
    }

    public static String getLongDateMiliString() {
        return ISO_DATE_FORMAT_LONG_MILI.format(getDateToday());
    }

    public static OffsetDateTime localDateTimeNow() {
        return OffsetDateTime.of(now().toLocalDateTime(), ZoneOffset.UTC);
    }

    /**
     * @return - get month date as 01-2020 String
     */
    public static String getMonthYearNumericalString() {
        return now().toString().split("-")[1] + "/" + now().getYear();
    }

    /**
     * @return next week day as dd/MM/yyyy
     */
    public static String getNextDayOfWeekNumericalFormat() {
        LocalDateTime date = LocalDateTime.now();
        do {
            date = date.plusDays(1);
        } while (date.getDayOfWeek().getValue() >= 5);
        return date.format(ISO_DATE_FORMAT_NO_TIME);
    }

    public static String featureDateManager(String tableDate) {
        if (tableDate != null) {

            final String[] dateTableData = tableDate.split("\\s+");
            String switchType;
            int dateValue = 0;

            if (dateTableData.length > 1) {
                switchType = dateTableData[0] + " " + dateTableData[1];
                dateValue = Integer.parseInt(dateTableData[2]);
            } else {
                switchType = "Day";
            }

            final LocalDateTime date = LocalDateTime.now();

            switch (switchType) {
                case "Day +":
                    return date.plusDays(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
                case "Day -":
                    return date.minusDays(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
                case "Month +":
                    return date.plusMonths(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
                case "Month -":
                    return date.minusMonths(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
                case "Year +":
                    return date.plusYears(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
                case "Year -":
                    return date.minusYears(dateValue).format(ISO_DATE_FORMAT_NO_TIME);
                default:
                    return date.format(ISO_DATE_FORMAT_NO_TIME);
            }
        }
        return null;
    }

}
