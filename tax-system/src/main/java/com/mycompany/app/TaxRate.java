package com.mycompany.app;

import java.math.BigDecimal;

public record TaxRate(TaxKey key, BigDecimal rate) {
}
