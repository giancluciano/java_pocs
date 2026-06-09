package com.mycompany.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public record Product(String id, String name, String category, BigDecimal price, TaxType taxType) {

    public BigDecimal taxAmount(Tax tax, TaxRules rules) {
        if (rules.exempt(this, tax)) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return price.multiply(tax.rate()).setScale(2, RoundingMode.HALF_UP);
    }

    public String describe(Tax tax, TaxRules rules) {
        BigDecimal taxAmount = taxAmount(tax, rules);
        BigDecimal total = price.add(taxAmount);
        return "Product[id=%s, name=%s, price=%s, %s@%s(%s)=%s, total=%s]"
                .formatted(id, name, price, taxType, tax.stateCode(), tax.rate(), taxAmount, total);
    }

    public void calculate(TaxRegistry registry, TaxRules rules) {
        List<Tax> taxes = registry.findByTaxType(taxType);

        System.out.printf("Tax report for Product[id=%s, name=%s, price=%s, taxType=%s]%n",
                id, name, price, taxType);

        for (Tax tax : taxes) {
            BigDecimal taxAmount = taxAmount(tax, rules);
            BigDecimal total = price.add(taxAmount);
            System.out.printf("  state=%s, rate=%s, tax=%s, total=%s",
                    tax.stateCode(), tax.rate(), taxAmount, total);
        }
    }
}
