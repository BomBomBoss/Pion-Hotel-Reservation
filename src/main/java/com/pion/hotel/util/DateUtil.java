package com.pion.hotel.util;

import com.pion.hotel.exception.UserInputException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Pattern;

public class DateUtil {

    private final static Pattern delimiter1 = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");
    private final static String dateFormat1 = "dd-MM-yyyy";
    private final static Pattern delimiter2 = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
    private final static String dateFormat2 = "dd/MM/yyyy";
    private final static Pattern delimiter3 = Pattern.compile("^\\d{2}.\\d{2}.\\d{4}$");
    private final static String dateFormat3 = "dd.MM.yyyy";
    protected final static Map<Pattern, String> patterns;

    static {
        patterns = Map.of(delimiter1, dateFormat1,
                delimiter2, dateFormat2,
                delimiter3, dateFormat3);
    }

    public static LocalDate mapToDate(String dateFromRequest)
    {
        return patterns.entrySet().stream()
                .filter(entry -> entry.getKey().matcher(dateFromRequest).matches())
                .map(format -> LocalDate.parse(dateFromRequest, DateTimeFormatter.ofPattern(format.getValue())))
                .findFirst().orElseThrow(() -> new UserInputException(String.format("[%s] - incorrect date format. Please use one of the:[%s], [%s], [%s]",
                        dateFromRequest, dateFormat1, dateFormat2, dateFormat3)));
    }

    public static boolean isStartDateValid(LocalDate startDate)
    {
        return (startDate.isAfter(LocalDate.now()) || startDate.equals(LocalDate.now()));
    }

    public static boolean isEndDateBeforeToday(LocalDate endDate)
    {
        return endDate.isBefore(LocalDate.now());
    }



}
