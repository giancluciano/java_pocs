package com.mycompany.app;

@FunctionalInterface
public interface TaxRule {

    boolean exempts(Product product, Tax tax);
}
