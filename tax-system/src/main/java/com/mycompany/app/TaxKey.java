package com.mycompany.app;

import java.time.Year;

public record TaxKey(String productId, String stateCode, Year year) {
}
