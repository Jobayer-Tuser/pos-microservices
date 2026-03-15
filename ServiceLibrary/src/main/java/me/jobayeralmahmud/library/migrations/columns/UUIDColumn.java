package me.jobayeralmahmud.library.migrations.columns;

public class UUIDColumn extends Column<UUIDColumn> {

    public UUIDColumn(String name) { super(name); }

    @Override
    protected String sqlType() {
        return "BINARY(16)";
    }
}