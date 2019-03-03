package com.raphael.carvalho.android.popularmovies.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static final String DAY = "dd";
    public static final String MOUNTH = "MM";
    public static final String YEAR = "yyyy";

    public static final String OUTPUT_FORMAT = DAY + "/" + MOUNTH + "/" + YEAR;

    private DateUtil() {
    }

    public static String format(String date, String inputFormat, String outputFormat) throws ParseException {
        Date dateInput = new SimpleDateFormat(inputFormat, Locale.getDefault())
                .parse(date);

        return new SimpleDateFormat(outputFormat, Locale.getDefault())
                .format(dateInput);
    }
}
