package com.mycompany.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public record Product(String id, String name, String category, BigDecimal price, TaxType taxType) {

    public BigDecimal taxAmount(Tax tax) {
        return price.multiply(tax.rate()).setScale(2, RoundingMode.HALF_UP);
    }

    public String describe(Tax tax) {
        BigDecimal taxAmount = taxAmount(tax);
        BigDecimal total = price.add(taxAmount);
        return "Product[id=%s, name=%s, price=%s, %s@%s(%s)=%s, total=%s]"
                .formatted(id, name, price, taxType, tax.stateCode(), tax.rate(), taxAmount, total);
    }

    public void calculate(TaxRegistry registry) {
        List<Tax> taxes = registry.findByTaxType(taxType);

        System.out.printf("Tax report for Product[id=%s, name=%s, price=%s, taxType=%s]%n",
                id, name, price, taxType);


        for (Tax tax : taxes) {
            BigDecimal taxAmount = taxAmount(tax);
            BigDecimal total = price.add(taxAmount);
            System.out.printf("  state=%s, rate=%s, tax=%s, total=%s%n",
                    tax.stateCode(), tax.rate(), taxAmount, total);
        }
    }
}
