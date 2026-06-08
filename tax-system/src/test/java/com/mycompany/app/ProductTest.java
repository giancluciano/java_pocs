package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.List;

import org.junit.jupiter.api.Test;

class ProductTest {

    private static final Product LAPTOP =
            new Product("P-001", "Laptop", "Eletronic", new BigDecimal("100.00"), TaxType.ICMS);

    @Test
    void taxAmountMultipliesPriceByRateRoundedToTwoDecimals() {
        Tax tax = new Tax(TaxType.ICMS, "CA", Year.of(2026), new BigDecimal("0.065"));

        assertEquals(new BigDecimal("6.50"), LAPTOP.taxAmount(tax));
    }

    @Test
    void taxAmountRejectsMismatchedTaxType() {
        Tax pis = new Tax(TaxType.PIS, "CA", Year.of(2026), new BigDecimal("0.04"));

        assertThrows(IllegalArgumentException.class, () -> LAPTOP.taxAmount(pis));
    }

    @Test
    void calculatePrintsOneRowPerState() {
        TaxRegistry registry = new TaxRegistry(List.of(
                new Tax(TaxType.ICMS, "CA", Year.of(2026), new BigDecimal("0.06")),
                new Tax(TaxType.ICMS, "NY", Year.of(2026), new BigDecimal("0.10")),
                new Tax(TaxType.PIS, "CA", Year.of(2026), new BigDecimal("0.04"))
        ));

        String output = captureCalculate(LAPTOP, registry);

        assertTrue(output.contains("state=CA"), output);
        assertTrue(output.contains("state=NY"), output);
        // PIS tax for CA must not appear in an ICMS product report.
        assertEquals(1, countOccurrences(output, "state=CA"));
    }

    @Test
    void calculatePrintsNoticeWhenNoTaxesRegistered() {
        TaxRegistry registry = new TaxRegistry(List.of(
                new Tax(TaxType.PIS, "CA", Year.of(2026), new BigDecimal("0.04"))
        ));

        String output = captureCalculate(LAPTOP, registry);

        assertTrue(output.contains("no ICMS taxes registered"), output);
    }

    private static String captureCalculate(Product product, TaxRegistry registry) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            product.calculate(registry);
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0;
        int index = 0;
        while ((index = haystack.indexOf(needle, index)) != -1) {
            count++;
            index += needle.length();
        }
        return count;
    }
}
