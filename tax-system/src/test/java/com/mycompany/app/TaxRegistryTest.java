package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class TaxRegistryTest {

    @Test
    void findReturnsTaxForRegisteredKey() {
        Tax icms = new Tax(TaxType.ICMS, "SP", Year.of(2026), new BigDecimal("0.18"));
        Tax pis = new Tax(TaxType.PIS, "SP", Year.of(2026), new BigDecimal("0.0165"));
        TaxRegistry registry = new TaxRegistry(List.of(icms, pis));

        Optional<Tax> found = registry.find(new Tax.Key(TaxType.ICMS, "SP"));

        assertTrue(found.isPresent());
        assertEquals(icms, found.get());
    }
}
