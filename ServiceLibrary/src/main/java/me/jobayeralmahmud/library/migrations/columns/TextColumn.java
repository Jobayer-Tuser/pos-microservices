package me.jobayeralmahmud.library.migrations.columns;

public class TextColumn extends Column<TextColumn> {
    public TextColumn(String name) {
        super(name);
    }

    @Override
    protected String sqlType() {
        return "TEXT";
    }
}
