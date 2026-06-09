# Tax Rule Design

The tax rule implementation uses two classic patterns working together: the
**Strategy pattern** and the **Specification pattern**.

## Strategy Pattern

`TaxRule` is a strategy interface — it defines a single algorithm (does this
rule exempt a product?) that can have many interchangeable implementations:

```java
@FunctionalInterface
public interface TaxRule {
    boolean exempts(Product product, Tax tax);
}
```

`FloridaSmallValuePisExemption` is one concrete strategy. The key property:
**adding a new rule means adding a new class, not editing existing code.**
`Product.taxAmount`/`calculate` never mention Florida or PIS — they just ask
"are you exempt?" and react. This is the Open/Closed Principle in action: open
for extension (new `TaxRule` classes), closed for modification (calculation
logic stays untouched).

## Specification Pattern

`FloridaSmallValuePisExemption` is specifically a **boolean specification** — a
self-contained predicate that answers a yes/no business question about domain
objects:

```java
return tax.taxType() == TaxType.PIS
        && "FL".equals(tax.stateCode())
        && product.price().compareTo(THRESHOLD) < 0;
```

The whole "FL + PIS + price < 10" business rule lives in one named, testable
place rather than being scattered through `if` statements in the calculation
code. The name documents the rule.

## Composite layer

`TaxRules` is a thin **composite / aggregate** that combines many
specifications into one decision:

```java
public boolean exempt(Product product, Tax tax) {
    return rules.stream().anyMatch(rule -> rule.exempts(product, tax));
}
```

It treats "a collection of rules" the same way a caller treats "a single rule"
— `Product` calls `rules.exempt(...)` once and doesn't care whether there are
zero, one, or fifty rules behind it. The `anyMatch` is effectively an **OR
composition** (exempt if *any* rule says so). `TaxRules.defaults()` acts as a
small **factory** that wires up the default rule set.

## How it fits together

```
Product.calculate(registry, rules)
        │  "am I exempt from this tax?"
        ▼
TaxRules.exempt(product, tax)        ← composite: OR over all rules
        │
        ▼
TaxRule.exempts(product, tax)        ← strategy interface
        │
        ▼
FloridaSmallValuePisExemption        ← concrete specification
```

## Why this shape, beyond "it works"

- **Each rule is independently unit-testable** — `FloridaSmallValuePisExemptionTest`
  exercises just the predicate's boundaries (9.99 vs 10.00) with no calculation
  or printing involved.
- **Rules are data, not control flow** — because `TaxRules` holds a
  `List<TaxRule>`, you could inject different rule sets per environment/region
  without recompiling the calculator.
- **`@FunctionalInterface`** means a trivial rule can be a lambda
  (`(p, t) -> ...`) instead of a class, while complex named rules stay as
  classes — you pick the weight that fits.

## The honest trade-off

For a POC with one rule, this is more structure than strictly necessary — a
single `if` in `taxAmount` would compute the same result. The pattern pays off
only once you have several rules, region-specific rule sets, or rules that need
isolated testing. The cost is indirection: reading the code means hopping
through three files to see what actually happens. Worth it here because tax
rules are exactly the kind of thing that multiplies over time — but worth
naming the trade-off rather than pretending the abstraction is free.

One current limitation given the POC scope: rules can only *exempt* (boolean
on/off). If a future rule needs to *change* a rate or apply a partial discount
rather than zero it out, `TaxRule` would need to return an adjusted
amount/rate instead of a `boolean` — a natural evolution of the same Strategy
shape when you need it.
