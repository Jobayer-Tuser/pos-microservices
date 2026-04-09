package me.jobayeralmahmud.library.migrations.columns;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumColumn extends Column<EnumColumn> {
    private final String[] options;

    public EnumColumn(String name, String... options) {
        super(name);
        this.options = options;
    }

    public EnumColumn(String name, Enum<?>... values) {
        super(name);
        this.options = Arrays.stream(values).map(Enum::name).toArray(String[]::new);
    }

    @Override
    protected String sqlType() {
        String values = Arrays.stream(options)
                .map(o -> "'" + o + "'")
                .collect(Collectors.joining(", "));
        return "ENUM(" + values + ")";
    }
}