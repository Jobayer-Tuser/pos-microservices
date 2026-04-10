package me.jobayeralmahmud.library.migrations;

public class ColumnDefinition {

    // Core properties.
    private final String name;
    private final Object dataType;

    // Modifiers.
    private boolean nullable = false;
    private boolean unique = false;
    private boolean unsigned = false;
    private boolean primaryKey = false;
    private boolean autoIncrement = false;
    private boolean defaultCurrentTimestamp = false;

    // Default value can be of any type (String, Number, Boolean, etc.)
    private Object defaultValue;
    private String afterColumn;

    ColumnDefinition(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    ColumnDefinition(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    /**
     * Fluent modifiers.
     * @return this for chaining
     */
    public ColumnDefinition nullable() {
        this.nullable = true;
        return this;
    }

    public ColumnDefinition unique() {
        this.unique = true;
        return this;
    }

    public ColumnDefinition unsigned() {
        this.unsigned = true;
        return this;
    }

    public ColumnDefinition primaryKey() {
        this.primaryKey = true;
        return this;
    }

    public ColumnDefinition autoIncrement() {
        this.autoIncrement = true;
        return this;
    }

    public ColumnDefinition defaultCurrentTimestamp() {
        this.defaultCurrentTimestamp = true;
        return this;
    }

    public ColumnDefinition defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ColumnDefinition after(String columnName) {
        this.afterColumn = columnName;
        return this;
    }

    public String afterColumn() {
        return afterColumn;
    }

    // Render the SQL definition
    public String getSqlDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(dataType);

        if (unsigned) sb.append(" UNSIGNED");
        if (autoIncrement) sb.append(" AUTO_INCREMENT");
        if (primaryKey) sb.append(" PRIMARY KEY");
        sb.append(nullable ? " DEFAULT NULL" : " NOT NULL");
        if (unique) sb.append(" UNIQUE");
        if (defaultCurrentTimestamp) sb.append(" DEFAULT CURRENT_TIMESTAMP");
        else if (defaultValue != null) sb.append(" DEFAULT '").append(defaultValue).append("'");

        return sb.toString();
    }
}