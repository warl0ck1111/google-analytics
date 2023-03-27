package com.example.ga4demo.shared.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@UtilityClass
@Slf4j
public class DateUtils {

    private final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // DateFormat is mutable object and its not thread safe, so we must create a new dateFormat each time we need to format
    private DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyyMMdd");
    }

    // DateFormat is mutable object and its not thread safe, so we must create a new dateFormat each time we need to format
    private DateFormat getTimeFormat() {
        return new SimpleDateFormat("yyyyMMddHHmm");
    }

    public Integer getNbDaysBetweenDates(Date startDate, Date endDate) {
        return Integer.valueOf(String.valueOf(ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant())));
    }

    public String getSimpleDate(Date date) {
        if (date != null) {
            DateFormat df = getDateFormat();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            df.setTimeZone(tz);
            return df.format(date);
        } else {
            return null;
        }
    }

    public String getSimpleTime(Date date) {
        if (date != null) {
            DateFormat df = getTimeFormat();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            df.setTimeZone(tz);
            return df.format(date);
        } else {
            return null;
        }
    }

    public String getSimpleDateFromLocalDate(LocalDate localDate) {
        if (localDate != null) {
            return localDate.format(localDateFormatter);
        } else {
            return null;
        }
    }

    public Integer getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public Integer getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public Date parseDate(String dateAsString) {
        try {
            if (StringUtils.isBlank(dateAsString)) {
                return null;
            } else {
                if (StringUtils.containsIgnoreCase(dateAsString, "-")) {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(dateAsString.substring(0, 10));
                } else {
                    return new SimpleDateFormat("yyyyMMdd").parse(dateAsString.substring(0, 8));
                }
            }
        } catch (ParseException e) {
            System.out.println("parseDate/ unable to parse " + dateAsString);
            return null;
        }
    }

    public LocalDate parseLocalDate(String dateAsString) {
        try {
            if (StringUtils.isBlank(dateAsString)) {
                return null;
            } else {
                if (StringUtils.containsIgnoreCase(dateAsString, "-")) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return LocalDate.parse(dateAsString.substring(0, 10), dateTimeFormatter);
                } else {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    return LocalDate.parse(dateAsString.substring(0, 8), dateTimeFormatter);
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("parseLocalDate/ unable to parse " + dateAsString);
            return null;
        }
    }

    public Date buildExpiryDate(Date createdDate, Integer maxNumberRetentionDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(createdDate);
        c.add(Calendar.DAY_OF_MONTH, maxNumberRetentionDays);
        return c.getTime();
    }

    public LocalDate calculateDateFromPeriod(
            LocalDate baseDate, Integer timeValue, ChronoUnit timeUnit) {
        LocalDate targetDate = null;
        switch (timeUnit) {
            case DAYS:
                targetDate = (LocalDate) Period.ofDays(timeValue).subtractFrom(baseDate);
                break;
            case MONTHS:
                targetDate = (LocalDate) Period.ofMonths(timeValue).subtractFrom(baseDate);
                break;
            case YEARS:
                targetDate = (LocalDate) Period.ofYears(timeValue).subtractFrom(baseDate);
                break;
        }
        return targetDate;
    }

    public LocalDate getCurrentLocalDate() {
        Clock cl = Clock.systemUTC();
        return LocalDate.now(cl);
    }

    public String getTodayAsSimpleDate() {
        return getSimpleDate(new Date());
    }

    public String getTodayAsSimpleTime() {
        return getSimpleTime(new Date());
    }

    public String getTimeRoundedTo5Minutes() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int unroundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 5;
        calendar.add(Calendar.MINUTE, -mod);
        return getSimpleTime(calendar.getTime());

    }

    public String getSimpleDateFromTimeStampInSeconds(Long timeStampInSeconds) {
        if (timeStampInSeconds != null) {
            return getSimpleDate(new Date(timeStampInSeconds * 1000));
        } else {
            return null;
        }
    }

    public String mapSimpleDateCustomFormatToSimpleDate(String dateAsString, DateTimeFormatter localDateFormatter) {
        try {
            if (StringUtils.isNotBlank(dateAsString) && localDateFormatter != null) {
                return getSimpleDateFromLocalDate(LocalDate.parse(dateAsString, localDateFormatter));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isDelayPassed(Instant instant, int delayInMs) {
        if (instant == null) {
            return true;
        } else {
            long diff = Duration.between(instant, Instant.now()).toMillis();
            return (diff > delayInMs);
        }
    }

    public LocalDate getLocalDateFromDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public String getYearMonthKeyFromLocalDate(LocalDate localDate) {
        String simpleDate = DateUtils.getSimpleDateFromLocalDate(localDate);
        return getYearMonthKeyFromSimpleDate(simpleDate);
    }

    public String getYearMonthKeyFromDate(Date date) {
        String simpleDate = getSimpleDate(date);
        return getYearMonthKeyFromSimpleDate(simpleDate);
    }

    public String getYearMonthKeyFromSimpleDate(String simpleDate) {
        if (StringUtils.isNotBlank(simpleDate)) {
            return simpleDate.substring(0, simpleDate.length() - 2);
        } else {
            return null;
        }
    }

    public String getFormattedDate(Date date, Locale locale) {
        try {
            if (date != null) {
                LocalDate localDate = getLocalDateFromDate(date);
                if (localDate != null) {
                    return localDate.format(DateTimeFormatter
                            .ofLocalizedDate(FormatStyle.LONG)
                            .withLocale(locale));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFormattedDate(String dateAsString, Locale locale) {
        try {
            Date date = parseDate(dateAsString);
            if (date != null) {
                LocalDate localDate = getLocalDateFromDate(date);
                if (localDate != null) {
                    return localDate.format(DateTimeFormatter
                            .ofLocalizedDate(FormatStyle.LONG)
                            .withLocale(locale));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
