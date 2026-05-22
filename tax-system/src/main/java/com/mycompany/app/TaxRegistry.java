package com.mycompany.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class TaxRegistry {

    private final Map<Tax.Key, Tax> taxesByKey;

    public TaxRegistry(List<Tax> taxes) {
        Objects.requireNonNull(taxes, "taxes");
        Map<Tax.Key, Tax> map = new HashMap<>();
        for (Tax tax : taxes) {
            Tax.Key key = tax.key();
            if (map.putIfAbsent(key, tax) != null) {
                throw new IllegalArgumentException("Duplicate tax for key: " + key);
            }
        }
        this.taxesByKey = Map.copyOf(map);
    }

    public Optional<Tax> find(Tax.Key key) {
        return Optional.ofNullable(taxesByKey.get(key));
    }
}
