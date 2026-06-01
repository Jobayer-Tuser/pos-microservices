package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostgresqlGrammar implements SchemaGrammar {

    @Override
    public String compileCreateMigrationsTable() {
        return """
            CREATE TABLE IF NOT EXISTS schema_migrations (
                id BIGSERIAL PRIMARY KEY,
                migration_tables VARCHAR(255) NOT NULL UNIQUE,
                executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
    }

    @Override
    public String compileDropIfExists(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    @Override
    public String compileCreate(Blueprint blueprint) {
        Stream<String> definitions = blueprint.getColumns().stream().map(this::compileDefinition);
        Stream<String> constraints = blueprint.getColumns().stream()
                .map(c -> {
                    if (c instanceof ForeignKeyDefinition f) return compileForeignKeyConstraint(f);
                    return null;
                })
                .filter(Objects::nonNull);

        String finalQuery = Stream.concat(
                Stream.concat(definitions, constraints),
                blueprint.getMultiColumnUniques().stream()
        ).collect(Collectors.joining(", "));

        return String.format("CREATE TABLE %s (%s)", blueprint.getTableName(), finalQuery);
    }

    @Override
    public List<String> compileAlter(Blueprint blueprint) {
        List<String> statements = new ArrayList<>();
        String tableName = blueprint.getTableName();

        blueprint.getForeignKeysToDrop().stream()
                .map(fk -> String.format("ALTER TABLE %s DROP CONSTRAINT %s", tableName, fk))
                .forEach(statements::add);

        blueprint.getColumnsToDrop().stream()
                .map(col -> String.format("ALTER TABLE %s DROP COLUMN %s", tableName, col))
                .forEach(statements::add);

        blueprint.getColumns().forEach(column -> {
            statements.add(String.format("ALTER TABLE %s ADD COLUMN %s",
                    tableName, compileDefinition(column)));

            if (column instanceof ForeignKeyDefinition f) {
                String constraint = compileForeignKeyConstraint(f);
                if (constraint != null) {
                    statements.add(String.format("ALTER TABLE %s ADD %s", tableName, constraint));
                }
            }
        });

        return statements;
    }

    private String compileDefinition(Object column) {
        return switch (column) {
            case String s -> s;
            case ColumnDefinition c -> compileColumn(c);
            case EnumDefinition e -> compileEnum(e);
            case ForeignKeyDefinition f -> compileForeignKey(f);
            default -> throw new IllegalStateException("Unknown column type: " + column.getClass());
        };
    }

    @Override
    public String compileColumn(ColumnDefinition column) {
        var parts = new ArrayList<String>();
        String resolvedDataType = column.getDataType().toString();

        if (column.isAutoIncrement()) {
            if (resolvedDataType.contains("BIGINT")) resolvedDataType = "BIGSERIAL";
            else if (resolvedDataType.contains("INT")) resolvedDataType = "SERIAL";
        }

        parts.add(column.getName() + " " + resolvedDataType);

        if (column.isPrimaryKey()) parts.add("PRIMARY KEY");
        
        parts.add(column.isNullable() ? "DEFAULT NULL" : "NOT NULL");
        
        if (column.isUnique()) parts.add("UNIQUE");
        
        if (column.isDefaultCurrentTimestamp()) {
            parts.add("DEFAULT CURRENT_TIMESTAMP");
        } else if (column.getDefaultValue() != null) {
            if (column.getDefaultValue() instanceof String) {
                parts.add("DEFAULT '" + column.getDefaultValue() + "'");
            } else {
                parts.add("DEFAULT " + column.getDefaultValue());
            }
        }

        return String.join(" ", parts);
    }

    @Override
    public String compileEnum(EnumDefinition column) {
        String enumValues = Arrays.stream(column.getOptions())
                .map(o -> "'" + o + "'")
                .collect(Collectors.joining(", "));

        var parts = new ArrayList<String>();
        parts.add(column.getName() + " VARCHAR(255) CHECK (" + column.getName() + " IN (" + enumValues + "))");
        parts.add(column.isNullable() ? "DEFAULT NULL" : "NOT NULL");
        
        if (column.isUnique()) parts.add("UNIQUE");
        if (column.getDefaultValue() != null) {
            parts.add("DEFAULT '" + column.getDefaultValue() + "'");
        }

        return String.join(" ", parts);
    }

    @Override
    public String compileForeignKey(ForeignKeyDefinition column) {
        var parts = new ArrayList<String>();
        parts.add(column.getColumnName() + " " + (column.getKeyType().name().equals("INTEGER") ? "BIGINT" : "UUID"));
        parts.add(column.isNullable() ? "DEFAULT NULL" : "NOT NULL");
        
        if (column.isUnique()) parts.add("UNIQUE");
        else if (column.getDefaultValue() != null) parts.add("DEFAULT '" + column.getDefaultValue() + "'");

        return String.join(" ", parts);
    }

    @Override
    public String compileForeignKeyConstraint(ForeignKeyDefinition column) {
        if (!column.isReferencesTable()) return null;
        if (column.getReferencedTable() == null) {
            throw new IllegalStateException(String.format("Foreign key %s references table but no table set.", column.getColumnName()));
        }
        return String.format(
            "CONSTRAINT fk_%s_%s FOREIGN KEY (%s) REFERENCES %s (%s) ON UPDATE %s ON DELETE %s",
            column.getOwningTable(), column.getColumnName(), column.getColumnName(), 
            column.getReferencedTable(), column.getReferencedColumn(), 
            column.getOnUpdate(), column.getOnDelete()
        );
    }
}
