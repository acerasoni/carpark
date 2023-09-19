package com.acerasoni.carpark.service;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormattingServiceTest {

    private static final Instant TEST_INSTANT = Instant.parse("2023-09-18T22:39:48.502301400Z");
    private static final String PATTERN_WITH_MILLIS = "HH:mm:ss.SSSS";
    private static final String PATTERN_WITHOUT_MILLIS = "HH:mm:ss";
    private static final String GBP_CURRENCY_CODE = "GBP";
    private static final String USD_CURRENCY_CODE = "USD";

    private FormattingService formattingService;

    @Test
    void formatInstant_withMillisecondsPattern_thenFormatWithMilliseconds() {
        formattingService = new FormattingService(PATTERN_WITH_MILLIS, GBP_CURRENCY_CODE);

        assertEquals("23:39:48.5023",
                formattingService.formatInstant(TEST_INSTANT),
                "Failed to format Instant with milliseconds");
    }

    @Test
    void formatInstant_withoutMillisecondsPattern_thenFormatWithoutMilliseconds() {
        formattingService = new FormattingService(PATTERN_WITHOUT_MILLIS, GBP_CURRENCY_CODE);

        assertEquals("23:39:48",
                formattingService.formatInstant(TEST_INSTANT),
                "Failed to format Instant without milliseconds");
    }

    @Test
    void formatCurrency_withGbpCurrency_thenFormatToGbp() {
        final var amount = 23452.122;
        formattingService = new FormattingService(PATTERN_WITH_MILLIS, GBP_CURRENCY_CODE);

        assertEquals("£23,452.12",
                formattingService.formatCurrency(amount),
                "Failed to format amount to GBP"
        );
    }

    @Test
    void formatCurrency_withUsdCurrency_thenFormatToUsd() {
        final var amount = 93761.968;
        formattingService = new FormattingService(PATTERN_WITH_MILLIS, USD_CURRENCY_CODE);

        assertEquals("US$93,761.97",
                formattingService.formatCurrency(amount),
                "Failed to format amount to USD");
    }
}