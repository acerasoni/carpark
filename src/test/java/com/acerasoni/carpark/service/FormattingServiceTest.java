package com.acerasoni.carpark.service;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormattingServiceTest {

    private static final Instant TEST_INSTANT = Instant.parse("2023-09-18T22:39:48.502301400Z");
    private static final String GBP_CURRENCY_CODE = "GBP";
    private static final String USD_CURRENCY_CODE = "USD";

    @Test
    void formatInstantWithMilliseconds() {
        final var pattern = "HH:mm:ss.SSSS";

        assertEquals("23:39:48.5023", new FormattingService(pattern, GBP_CURRENCY_CODE).formatInstant(TEST_INSTANT));
    }

    @Test
    void formatInstantWithoutMilliseconds() {
        final var pattern = "HH:mm:ss";

        assertEquals("23:39:48", new FormattingService(pattern, GBP_CURRENCY_CODE).formatInstant(TEST_INSTANT));
    }

    @Test
    void formatCurrencyWithGbpCurrency() {
        final var amount = 23452.122;

        assertEquals("23,452.122", new FormattingService("HH:mm:ss", GBP_CURRENCY_CODE).formatCurrency(amount));
    }

    @Test
    void formatCurrencyWithUsdCurrency() {
        final var amount = 93761.968;

        assertEquals("93,761.968", new FormattingService("HH:mm:ss", USD_CURRENCY_CODE).formatCurrency(amount));

    }
}