package me.jobayeralmahmud.library.migrations;

public class ForeignKeyDefinition {
    public enum KeyType {
        INTEGER("BIGINT UNSIGNED"),
        UUID("BINARY(16)");

        private final String sqlType;
        KeyType(String sqlType) {
            this.sqlType = sqlType;
        }
    }

    /**
     * Core properties.
     */
    private String owningTable;
    private final String columnName;
    private final KeyType keyType;

    /**
     * Modifiers.
     */
    private boolean nullable = false;
    private boolean unique = false;
    private String afterColumn;
    private Object defaultValue;

    /**
     * Constrained fields.
     */
    private boolean constrained = false;
    private String referencedTable = null;
    private String referencedColumn = "id";
    private String onUpdate = "CASCADE";
    private String onDelete = "RESTRICT";

    ForeignKeyDefinition(String columnName, KeyType keyType) {
        this.columnName = columnName;
        this.keyType = keyType;
    }

    public void owningTable(String tableName) {
        this.owningTable = tableName;
    }

    public ForeignKeyDefinition nullable() {
        this.nullable = true;
        return this;
    }

    public ForeignKeyDefinition unique() {
        this.unique = true;
        return this;
    }

    public ForeignKeyDefinition after(String columnName) {
        this.afterColumn = columnName;
        return this;
    }

    public ForeignKeyDefinition defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ForeignKeyDefinition constrained() {
        this.constrained = true;
        String base = columnName.endsWith("_id") ? columnName.substring(0, columnName.length() - 3) : columnName;
        this.referencedTable = base + "s";
        return this;
    }

    public ForeignKeyDefinition constrained(String referencedTable) {
        this.constrained = true;
        this.referencedTable = referencedTable;
        return this;
    }

    public ForeignKeyDefinition references(String referencedColumn) {
        this.referencedColumn = referencedColumn;
        return this;
    }

    public ForeignKeyDefinition onDeleteCascade() {
        this.onDelete = "CASCADE";
        return this;
    }

    public ForeignKeyDefinition onDeleteSetNull() {
        this.onDelete = "SET NULL";
        return this;
    }

    public ForeignKeyDefinition onDeleteRestrict() {
        this.onDelete = "RESTRICT";
        return this;
    }

    public ForeignKeyDefinition onUpdateCascade() {
        this.onUpdate = "CASCADE";
        return this;
    }

    public ForeignKeyDefinition onUpdateSetNull() {
        this.onUpdate = "SET NULL";
        return this;
    }

    public ForeignKeyDefinition onUpdateRestrict() {
        this.onUpdate = "RESTRICT";
        return this;
    }

    public String afterColumn() {
        return afterColumn;
    }

    public String getSqlDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append(columnName).append(" ").append(keyType.sqlType);

        sb.append(nullable ? " DEFAULT NULL" : " NOT NULL");
        if (unique) sb.append(" UNIQUE");
        else if (defaultValue != null) sb.append(" DEFAULT '").append(defaultValue).append("'");

        return sb.toString();
    }

    public String getConstraintSql() {
        if (!constrained)
            return null;
        return String.format(
            "CONSTRAINT fk_%s_%s FOREIGN KEY (%s) REFERENCES %s(%s) ON UPDATE %s ON DELETE %s",
            owningTable, columnName, columnName, referencedTable, referencedColumn, onUpdate, onDelete
        );
    }
}