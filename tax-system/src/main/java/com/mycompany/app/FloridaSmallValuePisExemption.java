package com.mycompany.app;

import java.math.BigDecimal;

/**
 * In Florida, PIS tax is waived when the product price is strictly below 10.
 */
public final class FloridaSmallValuePisExemption implements TaxRule {

    private static final BigDecimal THRESHOLD = new BigDecimal("10");

    @Override
    public boolean exempts(Product product, Tax tax) {
        return tax.taxType() == TaxType.PIS
                && "FL".equals(tax.stateCode())
                && product.price().compareTo(THRESHOLD) < 0;
    }
}
