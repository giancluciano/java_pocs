package com.mycompany.app;

public record Product(String id, String name, String category, TaxType taxType, String stateCode) {

    public Tax.Key taxKey() {
        return new Tax.Key(taxType, stateCode);
    }
}
