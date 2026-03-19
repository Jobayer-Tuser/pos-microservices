package me.jobayeralmahmud.library.migrations.columns;

public class IntegerColumn extends IntegerLikeColumn<IntegerColumn> {
    public IntegerColumn(String name) {
        super(name);
    }

    @Override
    protected String sqlType() {
        return "INT" + unsignedSuffix();
    }
}