package uk.co.caeldev.builder4test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FixedSizeListBuilder<K> implements OverrideField<FixedSizeListBuilder<K>>{

    private final int size;
    private final Creator<K> creator;
    private final Map<Field, Supplier> suppliers;
    private final Map<Field, Optional> values;

    private FixedSizeListBuilder(int size, Creator<K> creator) {
        this.size = size;
        this.creator = creator;
        values = new HashMap<>();
        suppliers = new HashMap<>();

    }

    protected static <U> FixedSizeListBuilder<U> fixedSizeListBuilder(int size, Creator<U> creator) {
        return new FixedSizeListBuilder<>(size, creator);
    }

    public <U> FixedSizeListBuilder<K> override(Field<U> field, Supplier<U> supplier) {
        suppliers.put(field, supplier);
        return this;
    }

    @Override
    public <U> FixedSizeListBuilder<K> override(Field<U> field, U value) {
        values.put(field, Optional.of(value));
        return this;
    }

    @Override
    public <U> FixedSizeListBuilder<K> override(Field<U> field, Creator<U> creator) {
        override(field, creator.build(new DefaultLookUp(values)));
        return this;
    }

    public List<K> get() {
        LookUp lookUp = new SupplierLookUp(values, suppliers);
        return IntStream.rangeClosed(1, size)
                .mapToObj(it -> EntityBuilder.entityBuilder(creator, lookUp).get())
                .collect(Collectors.toList());
    }
}
