package com.raphael.carvalho.android.popularmovies.util;

import java.util.Locale;

public class FormatUtil {
    private FormatUtil() {
    }

    public static String format(Float f) {
        return String.format(Locale.getDefault(), "%.2f", f);
    }
}
