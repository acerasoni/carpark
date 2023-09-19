package com.acerasoni.carpark.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

@Service
public class FormattingService {

    private final DateTimeFormatter instantFormatter;
    private final NumberFormat currencyFormatter;

    public FormattingService(@Value("${date-format}") final String dateFormat,
                             @Value("${currency}") final String currency) {
        this.currencyFormatter = NumberFormat.getCurrencyInstance();
        this.currencyFormatter.setCurrency(Currency.getInstance(currency));

        this.instantFormatter = DateTimeFormatter
                .ofPattern(dateFormat)
                .withZone(ZoneId.systemDefault());
    }

    public String formatInstant(final Instant instant) {
        return instantFormatter.format(instant);
    }

    public String formatCurrency(final double amount) {
        return currencyFormatter.format(amount);
    }
}
