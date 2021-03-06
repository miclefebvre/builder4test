package uk.co.caeldev.builder4test;

import uk.org.fyodor.generators.Generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FixedSizeListBuilder<K> implements OverrideField<FixedSizeListBuilder<K>>{

    private final int size;
    private final Creator<K> creator;
    private final Map<Field, Generator> generators;
    private final Map<Field, Optional> values;

    private FixedSizeListBuilder(int size, Creator<K> creator) {
        this.size = size;
        this.creator = creator;
        values = new HashMap<>();
        generators = new HashMap<>();

    }

    protected static <U> FixedSizeListBuilder<U> fixedSizeListBuilder(int size, Creator<U> creator) {
        return new FixedSizeListBuilder<>(size, creator);
    }

    public <U> FixedSizeListBuilder<K> override(Field<U> field, Generator<U> generator) {
        generators.put(field, generator);
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
        LookUp lookUp = new RandomLookUp(values, generators);
        return IntStream.rangeClosed(1, size)
                .mapToObj(it -> EntityBuilder.entityBuilder(creator, lookUp).get())
                .collect(Collectors.toList());
    }
}
