package me.jobayeralmahmud.library.migrations.factory;

import net.datafaker.Faker;

import java.util.List;
import java.util.stream.Stream;

public abstract class Factory<T> {

    protected final Faker faker = new Faker();

    public abstract T definition();

    public List<T> create(int count) {
        return Stream.generate(this::definition)
                .limit(count)
                .toList();
    }
}
