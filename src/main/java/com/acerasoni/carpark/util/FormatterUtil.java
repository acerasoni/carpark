package com.acerasoni.carpark.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatterUtil {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

    public static String formatInstant(final Instant instant, final String pattern) {
        return DateTimeFormatter
                .ofPattern(pattern)
                .withZone(ZoneId.systemDefault())
                .format(instant);
    }

    public static String formatCurrency(final double amount) {
        return CURRENCY_FORMAT.format(amount);
    }
}
