package me.jobayeralmahmud.library.migrations.columns;

public class JsonColumn extends Column<JsonColumn> {
    public JsonColumn(String name) {
        super(name);
    }

    @Override
    protected String sqlType() {
        return "JSON";
    }
}