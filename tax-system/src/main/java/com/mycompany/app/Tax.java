package com.mycompany.app;

import java.math.BigDecimal;
import java.time.Year;

public record Tax(String productId, String stateCode, Year year, BigDecimal rate, TaxType taxType) {
}
