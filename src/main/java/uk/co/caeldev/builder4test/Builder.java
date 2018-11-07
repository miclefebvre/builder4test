package uk.co.caeldev.builder4test;

public class Builder {

    Builder() {
    }

    public static Builder build() {
        return new Builder();
    }

    public <K> EntityBuilder<K> entity(Creator<K> creator) {
        return EntityBuilder.entityBuilder(creator);
    }
}