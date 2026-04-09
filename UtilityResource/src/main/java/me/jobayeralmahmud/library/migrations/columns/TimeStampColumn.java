package me.jobayeralmahmud.library.migrations.columns;

public class TimeStampColumn extends Column<TimeStampColumn> {
    private boolean useCurrentTimestamp = false;

    public TimeStampColumn(String name) {
        super(name);
    }

    @Override
    protected String sqlType() {
        return "TIMESTAMP";
    }

    public TimeStampColumn defaultCurrentTimestamp() {
        this.useCurrentTimestamp = true;
        return this;
    }

    @Override
    protected String formatDefault() {
        if (useCurrentTimestamp) {
            return " DEFAULT CURRENT_TIMESTAMP";
        }
        return super.formatDefault();
    }
}