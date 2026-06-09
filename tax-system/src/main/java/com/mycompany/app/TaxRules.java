package com.mycompany.app;

import java.util.List;

public record TaxRules(List<TaxRule> rules) {

    public TaxRules {
        rules = List.copyOf(rules);
    }

    public static TaxRules defaults() {
        return new TaxRules(List.of(new FloridaSmallValuePisExemption()));
    }

    public boolean exempt(Product product, Tax tax) {
        return rules.stream().anyMatch(rule -> rule.exempts(product, tax));
    }
}
