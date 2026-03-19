package me.jobayeralmahmud.library.migrations.columns;

public class BooleanColumn extends Column<BooleanColumn> {
    public BooleanColumn(String name) {
        super(name);
    }

    @Override
    protected String sqlType() {
        return "BOOLEAN";
    }
}
