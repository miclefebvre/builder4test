package uk.co.caeldev.builder4test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ElementBuilder<K> implements OverrideField<ElementBuilder<K>> {

    private final ElementListBuilder<K> elementListBuilder;
    private final Map<Field, Optional> fields;

    private ElementBuilder(ElementListBuilder<K> elementListBuilder) {
        this.elementListBuilder = elementListBuilder;
        this.fields = new HashMap<>();
    }

    public static <U> ElementBuilder<U> elementBuilder(ElementListBuilder<U> elementListBuilder) {
        return new ElementBuilder<>(elementListBuilder);
    }

    protected Map<Field, Optional> getFields() {
        return this.fields;
    }

    @Override
    public <U> ElementBuilder<K> override(Field<U> field, U value) {
        this.fields.put(field, Optional.ofNullable(value));
        return this;
    }

    @Override
    public <U> ElementBuilder<K> override(Field<U> field, Creator<U> creator) {
        return override(field, creator.build(new DefaultLookUp(fields)));
    }

    public <U> ElementBuilder<K> nullify(Field<U> field) {
        this.fields.put(field, Optional.empty());
        return this;
    }

    public ElementListBuilder<K> end() {
        return elementListBuilder;
    }
}
