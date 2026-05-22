package com.mycompany.app;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Product(String id, String name, String category, BigDecimal price, TaxType taxType, String stateCode) {

    public Tax.Key taxKey() {
        return new Tax.Key(taxType, stateCode);
    }

    public BigDecimal taxAmount(Tax tax) {
        if (!tax.key().equals(taxKey())) {
            throw new IllegalArgumentException("Tax " + tax.key() + " does not apply to product " + taxKey());
        }
        return price.multiply(tax.rate()).setScale(2, RoundingMode.HALF_UP);
    }

    public String describe(Tax tax) {
        BigDecimal taxAmount = taxAmount(tax);
        BigDecimal total = price.add(taxAmount);
        return "Product[id=%s, name=%s, price=%s, %s(%s)=%s, total=%s]"
                .formatted(id, name, price, taxType, tax.rate(), taxAmount, total);
    }
}
