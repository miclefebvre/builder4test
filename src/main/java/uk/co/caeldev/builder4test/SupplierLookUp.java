package uk.co.caeldev.builder4test;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SupplierLookUp extends LookUp {

    private Map<Field, Optional> values;
    private Map<Field, Supplier> suppliers;

    protected SupplierLookUp(Map<Field, Optional> values, Map<Field, Supplier> suppliers) {
        this.values = values;
        this.suppliers = suppliers;
    }

    @Override
    protected <V> void put(Field<V> field, V value) {
        values.put(field, Optional.ofNullable(value));
    }

    @Override
    public <V> V get(Field<V> field, V defaultValue) {
        Optional optValue = values.get(field);

        if (isNull(optValue)) {
            Supplier supplier = suppliers.get(field);

            if (nonNull(supplier)) {
                return (V)supplier.get();
            }

            return defaultValue;
        }

        if (optValue.isPresent()) {
            return (V)optValue.get();
        }

        return null;
    }

    @Override
    public <V> V get(Field<V> field) {
        return get(field, field.getDefaultValue());
    }
}
