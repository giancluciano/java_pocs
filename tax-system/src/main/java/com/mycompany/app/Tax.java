package com.mycompany.app;

import java.math.BigDecimal;
import java.time.Year;

public record Tax(TaxType taxType, String stateCode, Year year, BigDecimal rate) {

    public Key key() {
        return new Key(taxType, stateCode);
    }

    public record Key(TaxType taxType, String stateCode) {}
}
