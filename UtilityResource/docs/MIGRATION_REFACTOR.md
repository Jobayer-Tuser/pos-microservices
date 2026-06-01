# Comprehensive Code Refactor: Grammar Pattern Migration

This document contains the complete and full source code of all classes associated with the database migration refactor. It includes exactly what the code looked like entirely **before** the refactoring, and what it looks like entirely **after** (the latest implementation).

---

## Part 1: Newly Added Grammar Drivers

### 1. `SchemaGrammar.java`
This interface establishes the rules all database dialects must follow.
```java
package me.jobayeralmahmud.library.migrations;

import java.util.List;

public interface SchemaGrammar {
    String compileCreateMigrationsTable();
    String compileDropIfExists(String tableName);
    
    String compileCreate(Blueprint blueprint);
    List<String> compileAlter(Blueprint blueprint);
    
    String compileColumn(ColumnDefinition column);
    String compileEnum(EnumDefinition column);
    String compileForeignKey(ForeignKeyDefinition column);
    String compileForeignKeyConstraint(ForeignKeyDefinition column);
}
```

### 2. `MysqlGrammar.java`
Implements the interface applying localized MySQL rules natively.
```java
package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MysqlGrammar implements SchemaGrammar {

    @Override
    public String compileCreateMigrationsTable() {
        return """
            CREATE TABLE IF NOT EXISTS schema_migrations (
                id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
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
                .map(fk -> String.format("ALTER TABLE %s DROP FOREIGN KEY %s", tableName, fk))
                .forEach(statements::add);

        blueprint.getColumnsToDrop().stream()
                .map(col -> String.format("ALTER TABLE %s DROP COLUMN %s", tableName, col))
                .forEach(statements::add);

        blueprint.getColumns().forEach(column -> {
            String afterClause = getAfterClause(column);
            statements.add(String.format("ALTER TABLE %s ADD %s%s",
                    tableName, compileDefinition(column), afterClause));

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

    private String getAfterClause(Object column) {
        String after = switch (column) {
            case ColumnDefinition c -> c.getAfterColumn();
            case EnumDefinition e -> e.getAfterColumn();
            case ForeignKeyDefinition f -> f.getAfterColumn();
            default -> null;
        };
        return after != null ? " AFTER " + after : "";
    }

    @Override
    public String compileColumn(ColumnDefinition column) {
        var parts = new ArrayList<String>();
        parts.add(column.getName() + " " + column.getDataType());

        if (column.isUnsigned()) parts.add("UNSIGNED");
        if (column.isAutoIncrement()) parts.add("AUTO_INCREMENT");
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
        
        if (column.isOnUpdateCurrentTimestamp()) {
            parts.add("ON UPDATE CURRENT_TIMESTAMP");
        }

        return String.join(" ", parts);
    }

    @Override
    public String compileEnum(EnumDefinition column) {
        String enumValues = Arrays.stream(column.getOptions())
                .map(o -> "'" + o + "'")
                .collect(Collectors.joining(", "));

        var parts = new ArrayList<String>();
        parts.add(column.getName() + " ENUM(" + enumValues + ")");
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
        parts.add(column.getColumnName() + " " + (column.getKeyType().name().equals("INTEGER") ? "BIGINT UNSIGNED" : "BINARY(16)"));
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
```

### 3. `PostgresqlGrammar.java`
Implements PostgreSQL rules resolving unsupported types dynamically (like sequences and checks).
```java
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
```

---

## Part 2: Modified Class Files

### 4. `MigrationInitializer.java`

#### BEFORE
```java
package me.jobayeralmahmud.library.migrations;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Component
public class MigrationInitializer implements InitializingBean {

    private final DataSource dataSource;
    private final List<BaseMigration> migrations;

    public MigrationInitializer(DataSource dataSource, List<BaseMigration> migrations) {
        this.dataSource = dataSource;
        this.migrations = migrations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (migrations == null || migrations.isEmpty()) {
            System.out.println("No database migrations found to execute.");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            MigrationRunner runner = new MigrationRunner(connection);
            runner.run(migrations);
        }
    }
}
```

#### AFTER
```java
package me.jobayeralmahmud.library.migrations;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Component
public class MigrationInitializer implements InitializingBean {

    private final DataSource dataSource;
    private final List<BaseMigration> migrations;
    
    @Value("${database.type:mysql}")
    private String databaseType;

    public MigrationInitializer(DataSource dataSource, List<BaseMigration> migrations) {
        this.dataSource = dataSource;
        this.migrations = migrations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (migrations == null || migrations.isEmpty()) {
            System.out.println("No database migrations found to execute.");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            SchemaGrammar grammar;
            if (databaseType != null && databaseType.toLowerCase().contains("postgres")) {
                grammar = new PostgresqlGrammar();
            } else {
                grammar = new MysqlGrammar();
            }

            MigrationRunner runner = new MigrationRunner(connection, grammar);
            runner.run(migrations);
        }
    }
}
```

---

### 5. `MigrationRunner.java`

#### BEFORE
```java
package me.jobayeralmahmud.library.migrations;

import java.sql.*;
import java.util.*;

public class MigrationRunner {

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS schema_migrations (
                id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                migration_tables VARCHAR(255) NOT NULL UNIQUE,
                executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

    private static final String SELECT_MIGRATIONS_SQL = "SELECT migration_tables FROM schema_migrations ORDER BY id ASC";
    private static final String INSERT_MIGRATION_SQL = "INSERT INTO schema_migrations (migration_tables) VALUES (?)";
    private static final String DELETE_MIGRATION_SQL = "DELETE FROM schema_migrations WHERE migration_tables = ?";

    private final Connection connection;

    public MigrationRunner(Connection connection) {
        this.connection = connection;
    }

    public void run(List<BaseMigration> migrations) throws SQLException {
        ensureMigrationsTableExists();
        List<String> executed = getExecutedMigrations();

        migrations.stream()
                .sorted(Comparator.comparing(BaseMigration::migrationTablesName))
                .filter(m -> !executed.contains(m.migrationTablesName()))
                .forEach(m -> {
                    try {
                        info("Running migration: " + m.migrationTablesName());
                        runMigration(m);
                        recordMigration(m.migrationTablesName());
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                });
    }

    public void rollback(List<BaseMigration> migrations, int steps) throws SQLException {
        ensureMigrationsTableExists();

        List<String> executed = getExecutedMigrations();
        if (executed.isEmpty()) {
            info("No migrations to rollback.");
            return;
        }

        List<String> toRollback = new ArrayList<>(
                executed.subList(Math.max(0, executed.size() - steps), executed.size()));
        Collections.reverse(toRollback);

        for (String name : toRollback) {
            BaseMigration migration = migrations.stream()
                    .filter(m -> m.migrationTablesName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (migration == null) {
                error("Cannot rollback " + name + ": migration class not found.");
                continue;
            }

            info("Rolling back migration: " + name);
            withTransaction(schema -> {
                migration.down(schema);
                removeMigrationRecord(name);
                info("Rollback of " + name + " successful.");
            }, "Rollback failed: " + name);
        }
    }

    private void runMigration(BaseMigration migration) throws SQLException {
        withTransaction(schema -> migration.up(schema), "Migration failed: " + migration.migrationTablesName());
    }

    private void withTransaction(ThrowingConsumer<Schema> action, String errorMessage) throws SQLException {
        boolean originalAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            action.accept(new Schema(connection));
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw new SQLException(errorMessage, e);
        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    private void executePrepared(String sql, String value) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.executeUpdate();
            connection.commit();
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    private void ensureMigrationsTableExists() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        }
    }

    private List<String> getExecutedMigrations() throws SQLException {
        List<String> executed = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_MIGRATIONS_SQL)) {
            while (rs.next())
                executed.add(rs.getString("migration_tables"));
        }
        return executed;
    }

    private void recordMigration(String name) throws SQLException {
        executePrepared(INSERT_MIGRATION_SQL, name);
    }

    private void removeMigrationRecord(String name) throws SQLException {
        executePrepared(DELETE_MIGRATION_SQL, name);
    }

    private static void info(String message) { System.out.println(message); }
    private static void error(String message) { System.err.println(message); }

    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
}
```

#### AFTER
```java
package me.jobayeralmahmud.library.migrations;

import java.sql.*;
import java.util.*;

public class MigrationRunner {

    private static final String SELECT_MIGRATIONS_SQL = "SELECT migration_tables FROM schema_migrations ORDER BY id ASC";
    private static final String INSERT_MIGRATION_SQL = "INSERT INTO schema_migrations (migration_tables) VALUES (?)";
    private static final String DELETE_MIGRATION_SQL = "DELETE FROM schema_migrations WHERE migration_tables = ?";

    private final Connection connection;
    private final SchemaGrammar grammar;

    public MigrationRunner(Connection connection, SchemaGrammar grammar) {
        this.connection = connection;
        this.grammar = grammar;
    }

    public void run(List<BaseMigration> migrations) throws SQLException {
        ensureMigrationsTableExists();
        List<String> executed = getExecutedMigrations();

        migrations.stream()
                .sorted(Comparator.comparing(BaseMigration::migrationTablesName))
                .filter(m -> !executed.contains(m.migrationTablesName()))
                .forEach(m -> {
                    try {
                        info("Running migration: " + m.migrationTablesName());
                        runMigration(m);
                        recordMigration(m.migrationTablesName());
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                });
    }

    public void rollback(List<BaseMigration> migrations, int steps) throws SQLException {
        ensureMigrationsTableExists();

        List<String> executed = getExecutedMigrations();
        if (executed.isEmpty()) {
            info("No migrations to rollback.");
            return;
        }

        List<String> toRollback = new ArrayList<>(
                executed.subList(Math.max(0, executed.size() - steps), executed.size()));
        Collections.reverse(toRollback);

        for (String name : toRollback) {
            BaseMigration migration = migrations.stream()
                    .filter(m -> m.migrationTablesName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (migration == null) {
                error("Cannot rollback " + name + ": migration class not found.");
                continue;
            }

            info("Rolling back migration: " + name);
            withTransaction(schema -> {
                migration.down(schema);
                removeMigrationRecord(name);
                info("Rollback of " + name + " successful.");
            }, "Rollback failed: " + name);
        }
    }

    private void runMigration(BaseMigration migration) throws SQLException {
        withTransaction(schema -> migration.up(schema), "Migration failed: " + migration.migrationTablesName());
    }

    private void withTransaction(ThrowingConsumer<Schema> action, String errorMessage) throws SQLException {
        boolean originalAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try {
            action.accept(new Schema(connection, grammar));
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw new SQLException(errorMessage, e);
        } finally {
            connection.setAutoCommit(originalAutoCommit);
        }
    }

    private void executePrepared(String sql, String value) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, value);
            stmt.executeUpdate();
            connection.commit();
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    private void ensureMigrationsTableExists() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(grammar.compileCreateMigrationsTable());
        }
    }

    private List<String> getExecutedMigrations() throws SQLException {
        List<String> executed = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_MIGRATIONS_SQL)) {
            while (rs.next())
                executed.add(rs.getString("migration_tables"));
        }
        return executed;
    }

    private void recordMigration(String name) throws SQLException {
        executePrepared(INSERT_MIGRATION_SQL, name);
    }

    private void removeMigrationRecord(String name) throws SQLException {
        executePrepared(DELETE_MIGRATION_SQL, name);
    }

    private static void info(String message) { System.out.println(message); }
    private static void error(String message) { System.err.println(message); }

    @FunctionalInterface
    private interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }
}
```

---

### 6. `Schema.java`

#### BEFORE
```java
package me.jobayeralmahmud.library.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

public class Schema {

    private final Connection connection;

    public Schema(Connection connection) {
        this.connection = connection;
    }

    public void create(String tableName, Consumer<Blueprint> callback) throws SQLException {
        Blueprint table = new Blueprint(tableName);
        callback.accept(table);
        execute(table.getSql(tableName));
    }

    public void dropIfExists(String tableName) throws SQLException {
        execute("DROP TABLE IF EXISTS " + tableName);
    }

    public void table(String tableName, Consumer<Blueprint> callback) throws SQLException {
        Blueprint table = new Blueprint(tableName);
        callback.accept(table);

        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            for (String sql : table.getAlterationSql(tableName)) {
                statement.execute(sql);
                connection.commit();
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    private void execute(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            IO.println(sql);
            statement.execute(sql);
        }
    }
}
```

#### AFTER
```java
package me.jobayeralmahmud.library.migrations;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

public class Schema {

    private final Connection connection;
    private final SchemaGrammar grammar;

    public Schema(Connection connection, SchemaGrammar grammar) {
        this.connection = connection;
        this.grammar = grammar;
    }

    public void create(String tableName, Consumer<Blueprint> callback) throws SQLException {
        Blueprint table = new Blueprint(tableName);
        callback.accept(table);
        execute(grammar.compileCreate(table));
    }

    public void dropIfExists(String tableName) throws SQLException {
        execute(grammar.compileDropIfExists(tableName));
    }

    public void table(String tableName, Consumer<Blueprint> callback) throws SQLException {
        Blueprint table = new Blueprint(tableName);
        callback.accept(table);

        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            for (String sql : grammar.compileAlter(table)) {
                statement.execute(sql);
                connection.commit();
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    private void execute(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            IO.println(sql);
            statement.execute(sql);
        }
    }
}
```

---

### 7. `Blueprint.java`

#### BEFORE
```java
package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Blueprint {

    private final String tableName;
    private final List<Object> columns             = new ArrayList<>();
    private final List<String> columnsToDrop       = new ArrayList<>();
    private final List<String> foreignKeysToDrop   = new ArrayList<>();
    private final List<String> multiColumnUniques  = new ArrayList<>();

    public Blueprint(String tableName) {
        this.tableName = tableName;
    }

    public void id() { addColumn("id", DataType.BIGINT).unsigned().autoIncrement().primaryKey(); }
    public void uuid() { addColumn("id", DataType.BINARY(16)).primaryKey(); }
    public ColumnDefinition uuid(String name) { return addColumn(name, DataType.BINARY(16)); }
    public ColumnDefinition tinyInteger(String name) { return addColumn(name, DataType.TINYINT); }
    public ColumnDefinition integer(String name) { return addColumn(name, DataType.INT); }
    public ColumnDefinition bigInteger(String name) { return addColumn(name, DataType.BIGINT); }
    public ColumnDefinition decimal(String name, int precision, int scale) { return addColumn(name, DataType.DECIMAL(precision, scale)); }
    public ColumnDefinition decimal(String name) { return decimal(name, 12, 2); }
    public ColumnDefinition numeric(String name, int precision, int scale) { return addColumn(name, DataType.NUMERIC(precision, scale)); }
    public ColumnDefinition numeric(String name) { return numeric(name, 10, 0); }
    public ColumnDefinition double_(String name) { return addColumn(name, DataType.DOUBLE); }
    public ColumnDefinition string(String name, int length) { return addColumn(name, DataType.VARCHAR(length)); }
    public ColumnDefinition string(String name) { return string(name, 255); }
    public ColumnDefinition text(String name) { return addColumn(name, DataType.TEXT); }
    public ColumnDefinition json(String name) { return addColumn(name, DataType.JSON); }
    public ColumnDefinition date(String name) { return addColumn(name, DataType.DATE); }
    public ColumnDefinition dateTime(String name) { return addColumn(name, DataType.DATETIME); }
    public ColumnDefinition timestamp(String name) { return addColumn(name, DataType.TIMESTAMP); }
    public ColumnDefinition bool(String name) { return addColumn(name, DataType.BOOLEAN); }

    public ForeignKeyDefinition foreignUuid(String name) {
        var col = new ForeignKeyDefinition(name, ForeignKeyDefinition.KeyType.UUID);
        col.owningTable(this.tableName);
        columns.add(col);
        return col;
    }

    public ForeignKeyDefinition foreignId(String name) {
        var col = new ForeignKeyDefinition(name, ForeignKeyDefinition.KeyType.INTEGER);
        col.owningTable(this.tableName);
        columns.add(col);
        return col;
    }

    public EnumDefinition enumeration(String name, String... options) {
        var col = new EnumDefinition(name, options);
        columns.add(col);
        return col;
    }

    public EnumDefinition enumeration(String name, Enum<?>... values) {
        var col = new EnumDefinition(name, values);
        columns.add(col);
        return col;
    }

    public void timestamps() {
        addColumn("created_at", DataType.TIMESTAMP).nullable().defaultCurrentTimestamp();
        addColumn("updated_at", DataType.TIMESTAMP).nullable().defaultCurrentTimestamp().onUpdateCurrentTimestamp();
    }

    public void softDelete() {
        addColumn("deleted_at", DataType.TIMESTAMP).nullable();
    }

    public void unique(String... columnNames) {
        multiColumnUniques.add("UNIQUE (" + String.join(", ", columnNames) + ")");
    }

    public void dropColumn(String name) {
        columnsToDrop.add(name);
    }

    public void dropForeign(String columnName) {
        foreignKeysToDrop.add(String.format("fk_%s_%s", tableName, columnName));
    }

    public String getSql(String tableName) {
        Stream<String> definitions = columns.stream().map(this::getDefinition);
        Stream<String> constraints = columns.stream()
                .map(c -> {
                    if (c instanceof ForeignKeyDefinition f) return f.getConstraintSql();
                    return null;
                })
                .filter(Objects::nonNull);

        String finalQuery = Stream.concat(
                Stream.concat(definitions, constraints),
                multiColumnUniques.stream()
        ).collect(Collectors.joining(", "));

        return String.format("CREATE TABLE %s (%s)", tableName, finalQuery);
    }

    public List<String> getAlterationSql(String tableName) {
        List<String> statements = new ArrayList<>();

        foreignKeysToDrop.stream()
                .map(fk -> String.format("ALTER TABLE %s DROP FOREIGN KEY %s", tableName, fk))
                .forEach(statements::add);

        columnsToDrop.stream()
                .map(col -> String.format("ALTER TABLE %s DROP COLUMN %s", tableName, col))
                .forEach(statements::add);

        columns.forEach(column -> {
            String afterClause = getAfterClause(column);
            statements.add(String.format("ALTER TABLE %s ADD %s%s",
                    tableName, getDefinition(column), afterClause));

            if (column instanceof ForeignKeyDefinition f) {
                String constraint = f.getConstraintSql();
                if (constraint != null) {
                    statements.add(String.format("ALTER TABLE %s ADD %s", tableName, constraint));
                }
            }
        });

        return statements;
    }

    private ColumnDefinition addColumn(String name, DataType sqlType) {
        var col = new ColumnDefinition(name, sqlType);
        columns.add(col);
        return col;
    }

    private ColumnDefinition addColumn(String name, String sqlType) {
        var col = new ColumnDefinition(name, sqlType);
        columns.add(col);
        return col;
    }

    private String getDefinition(Object column) {
        return switch (column) {
            case String s -> s;
            case ColumnDefinition c -> c.getSqlDefinition();
            case EnumDefinition e -> e.getSqlDefinition();
            case ForeignKeyDefinition f -> f.getSqlDefinition();
            default -> throw new IllegalStateException("Unknown column type: " + column.getClass());
        };
    }

    private String getAfterClause(Object column) {
        String after = switch (column) {
            case ColumnDefinition c -> c.afterColumn();
            case EnumDefinition e -> e.afterColumn();
            case ForeignKeyDefinition f -> f.afterColumn();
            default -> null;
        };

        return after != null ? " AFTER " + after : "";
    }
}
```

#### AFTER
```java
package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Blueprint {

    private final String tableName;
    private final List<Object> columns = new ArrayList<>();
    private final List<String> columnsToDrop = new ArrayList<>();
    private final List<String> foreignKeysToDrop = new ArrayList<>();
    private final List<String> multiColumnUniques = new ArrayList<>();

    public Blueprint(String tableName) {
        this.tableName = tableName;
    }

    public void id() { addColumn("id", DataType.BIGINT).unsigned().autoIncrement().primaryKey(); }
    public void uuid() { addColumn("id", DataType.BINARY(16)).primaryKey(); }
    public ColumnDefinition uuid(String name) { return addColumn(name, DataType.BINARY(16)); }
    public ColumnDefinition tinyInteger(String name) { return addColumn(name, DataType.TINYINT); }
    public ColumnDefinition integer(String name) { return addColumn(name, DataType.INT); }
    public ColumnDefinition bigInteger(String name) { return addColumn(name, DataType.BIGINT); }
    public ColumnDefinition decimal(String name, int precision, int scale) { return addColumn(name, DataType.DECIMAL(precision, scale)); }
    public ColumnDefinition decimal(String name) { return decimal(name, 12, 2); }
    public ColumnDefinition numeric(String name, int precision, int scale) { return addColumn(name, DataType.NUMERIC(precision, scale)); }
    public ColumnDefinition numeric(String name) { return numeric(name, 10, 0); }
    public ColumnDefinition double_(String name) { return addColumn(name, DataType.DOUBLE); }
    public ColumnDefinition string(String name, int length) { return addColumn(name, DataType.VARCHAR(length)); }
    public ColumnDefinition string(String name) { return string(name, 255); }
    public ColumnDefinition text(String name) { return addColumn(name, DataType.TEXT); }
    public ColumnDefinition json(String name) { return addColumn(name, DataType.JSON); }
    public ColumnDefinition date(String name) { return addColumn(name, DataType.DATE); }
    public ColumnDefinition dateTime(String name) { return addColumn(name, DataType.DATETIME); }
    public ColumnDefinition timestamp(String name) { return addColumn(name, DataType.TIMESTAMP); }
    public ColumnDefinition bool(String name) { return addColumn(name, DataType.BOOLEAN); }

    public ForeignKeyDefinition foreignUuid(String name) {
        var col = new ForeignKeyDefinition(name, ForeignKeyDefinition.KeyType.UUID);
        col.owningTable(this.tableName);
        columns.add(col);
        return col;
    }

    public ForeignKeyDefinition foreignId(String name) {
        var col = new ForeignKeyDefinition(name, ForeignKeyDefinition.KeyType.INTEGER);
        col.owningTable(this.tableName);
        columns.add(col);
        return col;
    }

    public EnumDefinition enumeration(String name, String... options) {
        var col = new EnumDefinition(name, options);
        columns.add(col);
        return col;
    }

    public EnumDefinition enumeration(String name, Enum<?>... values) {
        var col = new EnumDefinition(name, values);
        columns.add(col);
        return col;
    }

    public void timestamps() {
        addColumn("created_at", DataType.TIMESTAMP).nullable().defaultCurrentTimestamp();
        addColumn("updated_at", DataType.TIMESTAMP).nullable().defaultCurrentTimestamp().onUpdateCurrentTimestamp();
    }

    public void softDelete() {
        addColumn("deleted_at", DataType.TIMESTAMP).nullable();
    }

    public void unique(String... columnNames) {
        multiColumnUniques.add("UNIQUE (" + String.join(", ", columnNames) + ")");
    }

    public void dropColumn(String name) {
        columnsToDrop.add(name);
    }

    public void dropForeign(String columnName) {
        foreignKeysToDrop.add(String.format("fk_%s_%s", tableName, columnName));
    }

    private ColumnDefinition addColumn(String name, DataType sqlType) {
        var col = new ColumnDefinition(name, sqlType);
        columns.add(col);
        return col;
    }

    private ColumnDefinition addColumn(String name, String sqlType) {
        var col = new ColumnDefinition(name, sqlType);
        columns.add(col);
        return col;
    }

    public String getTableName() { return tableName; }
    public List<Object> getColumns() { return columns; }
    public List<String> getColumnsToDrop() { return columnsToDrop; }
    public List<String> getForeignKeysToDrop() { return foreignKeysToDrop; }
    public List<String> getMultiColumnUniques() { return multiColumnUniques; }
}
```

---

### 8. `ColumnDefinition.java`

#### BEFORE
```java
package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;

public class ColumnDefinition {

    private final String name;
    private final Object dataType;

    private boolean nullable = false;
    private boolean unique = false;
    private boolean unsigned = false;
    private boolean primaryKey = false;
    private boolean autoIncrement = false;
    private boolean defaultCurrentTimestamp = false;
    private boolean onUpdateCurrentTimestamp = false;

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

    public ColumnDefinition nullable() { this.nullable = true; return this; }
    public ColumnDefinition unique() { this.unique = true; return this; }
    public ColumnDefinition unsigned() { this.unsigned = true; return this; }
    public ColumnDefinition primaryKey() { this.primaryKey = true; return this; }
    public ColumnDefinition autoIncrement() { this.autoIncrement = true; return this; }
    public ColumnDefinition defaultCurrentTimestamp() { this.defaultCurrentTimestamp = true; return this; }
    public ColumnDefinition onUpdateCurrentTimestamp() { this.onUpdateCurrentTimestamp = true; return this; }
    public ColumnDefinition defaultValue(Object defaultValue) { this.defaultValue = defaultValue; return this; }
    public ColumnDefinition after(String columnName) { this.afterColumn = columnName; return this; }
    public String afterColumn() { return afterColumn; }

    public String getSqlDefinition() {
        var parts = new ArrayList<String>();
        parts.add(name + " " + dataType);

        if (unsigned) parts.add("UNSIGNED");
        if (autoIncrement) parts.add("AUTO_INCREMENT");
        if (primaryKey) parts.add("PRIMARY KEY");
        parts.add(nullable ? "DEFAULT NULL" : "NOT NULL");
        if (unique) parts.add("UNIQUE");
        if (defaultCurrentTimestamp) parts.add("DEFAULT CURRENT_TIMESTAMP");
        else if (defaultValue != null) parts.add("DEFAULT " + defaultValue);
        if (onUpdateCurrentTimestamp) parts.add("ON UPDATE CURRENT_TIMESTAMP");

        return String.join(" ", parts);
    }
}
```

#### AFTER
```java
package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;

public class ColumnDefinition {

    private final String name;
    private final Object dataType;

    private boolean nullable = false;
    private boolean unique = false;
    private boolean unsigned = false;
    private boolean primaryKey = false;
    private boolean autoIncrement = false;
    private boolean defaultCurrentTimestamp = false;
    private boolean onUpdateCurrentTimestamp = false;

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

    public ColumnDefinition nullable() { this.nullable = true; return this; }
    public ColumnDefinition unique() { this.unique = true; return this; }
    public ColumnDefinition unsigned() { this.unsigned = true; return this; }
    public ColumnDefinition primaryKey() { this.primaryKey = true; return this; }
    public ColumnDefinition autoIncrement() { this.autoIncrement = true; return this; }
    public ColumnDefinition defaultCurrentTimestamp() { this.defaultCurrentTimestamp = true; return this; }
    public ColumnDefinition onUpdateCurrentTimestamp() { this.onUpdateCurrentTimestamp = true; return this; }
    public ColumnDefinition defaultValue(Object defaultValue) { this.defaultValue = defaultValue; return this; }
    public ColumnDefinition after(String columnName) { this.afterColumn = columnName; return this; }

    public String getAfterColumn() { return afterColumn; }
    public String getName() { return name; }
    public Object getDataType() { return dataType; }
    public boolean isNullable() { return nullable; }
    public boolean isUnique() { return unique; }
    public boolean isUnsigned() { return unsigned; }
    public boolean isPrimaryKey() { return primaryKey; }
    public boolean isAutoIncrement() { return autoIncrement; }
    public boolean isDefaultCurrentTimestamp() { return defaultCurrentTimestamp; }
    public boolean isOnUpdateCurrentTimestamp() { return onUpdateCurrentTimestamp; }
    public Object getDefaultValue() { return defaultValue; }
}
```

---

### 9. `EnumDefinition.java`

#### BEFORE
```java
package me.jobayeralmahmud.library.migrations;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumDefinition {

    private final String name;
    private final String[] options;

    private boolean nullable = false;
    private boolean unique = false;
    private String afterColumn = null;
    private String defaultValue = null;

    EnumDefinition(String columnName, String... options) {
        this.name = columnName;
        this.options = options;
    }

    EnumDefinition(String columnName, Enum<?>... options) {
        this.name = columnName;
        this.options = Arrays.stream(options).map(Enum::name).toArray(String[]::new);
    }

    public EnumDefinition nullable() { this.nullable = true; return this; }
    public EnumDefinition unique() { this.unique = true; return this; }
    public EnumDefinition after(String columnName) { this.afterColumn = columnName; return this; }
    public EnumDefinition defaultValue(String defaultValue) { this.defaultValue = defaultValue; return this; }
    public String afterColumn() { return afterColumn; }

    public String getSqlDefinition() {
        String enumValues = Arrays.stream(options)
                .map(o -> "'" + o + "'")
                .collect(Collectors.joining(", "));

        var parts = new java.util.ArrayList<String>();

        parts.add(name + " ENUM(" + enumValues + ")");
        parts.add(nullable ? "DEFAULT NULL" : "NOT NULL");
        if (unique)
            parts.add("UNIQUE");
        if (defaultValue != null)
            parts.add("DEFAULT '" + defaultValue + "'");

        return String.join(" ", parts);
    }
}
```

#### AFTER
```java
package me.jobayeralmahmud.library.migrations;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumDefinition {

    private final String name;
    private final String[] options;

    private boolean nullable = false;
    private boolean unique = false;
    private String afterColumn = null;
    private String defaultValue = null;

    EnumDefinition(String columnName, String... options) {
        this.name = columnName;
        this.options = options;
    }

    EnumDefinition(String columnName, Enum<?>... options) {
        this.name = columnName;
        this.options = Arrays.stream(options).map(Enum::name).toArray(String[]::new);
    }

    public EnumDefinition nullable() { this.nullable = true; return this; }
    public EnumDefinition unique() { this.unique = true; return this; }
    public EnumDefinition after(String columnName) { this.afterColumn = columnName; return this; }
    public EnumDefinition defaultValue(String defaultValue) { this.defaultValue = defaultValue; return this; }

    public String getAfterColumn() { return afterColumn; }
    public String getName() { return name; }
    public String[] getOptions() { return options; }
    public boolean isNullable() { return nullable; }
    public boolean isUnique() { return unique; }
    public String getDefaultValue() { return defaultValue; }
}
```

---

### 10. `ForeignKeyDefinition.java`

#### BEFORE
```java
package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;

public class ForeignKeyDefinition {
    public enum KeyType {
        INTEGER("BIGINT UNSIGNED"),
        UUID("BINARY(16)");

        private final String sqlType;

        KeyType(String sqlType) {
            this.sqlType = sqlType;
        }
    }

    private String owningTable;
    private final String columnName;
    private final KeyType keyType;

    private boolean nullable = false;
    private boolean unique = false;
    private String afterColumn;
    private Object defaultValue;

    private boolean referencesTable = false;
    private String referencedTable = null;
    private String referencedColumn = "id";
    private String onUpdate = "CASCADE";
    private String onDelete = "RESTRICT";

    ForeignKeyDefinition(String columnName, KeyType keyType) {
        this.columnName = columnName;
        this.keyType = keyType;
    }

    public void owningTable(String tableName) { this.owningTable = tableName; }
    public ForeignKeyDefinition nullable() { this.nullable = true; return this; }
    public ForeignKeyDefinition unique() { this.unique = true; return this; }
    public ForeignKeyDefinition after(String columnName) { this.afterColumn = columnName; return this; }
    public ForeignKeyDefinition defaultValue(Object defaultValue) { this.defaultValue = defaultValue; return this; }
    public ForeignKeyDefinition referencesTable(String table) { this.referencesTable = true; this.referencedTable = table; return this; }
    public ForeignKeyDefinition referencesColumn(String column) { this.referencedColumn = column; return this; }
    public ForeignKeyDefinition onDeleteCascade() { this.onDelete = "CASCADE"; return this; }
    public ForeignKeyDefinition onDeleteSetNull() { this.onDelete = "SET NULL"; return this; }
    public ForeignKeyDefinition onDeleteRestrict() { this.onDelete = "RESTRICT"; return this; }
    public ForeignKeyDefinition onUpdateCascade() { this.onUpdate = "CASCADE"; return this; }
    public ForeignKeyDefinition onUpdateSetNull() { this.onUpdate = "SET NULL"; return this; }
    public ForeignKeyDefinition onUpdateRestrict() { this.onUpdate = "RESTRICT"; return this; }
    public String afterColumn() { return afterColumn; }

    public String getSqlDefinition() {
        var parts = new ArrayList<String>();

        parts.add(columnName + " " + keyType.sqlType);
        parts.add(nullable ? "DEFAULT NULL" : "NOT NULL");
        if (unique) parts.add("UNIQUE");
        else if (defaultValue != null) parts.add("DEFAULT '" + defaultValue + "'");

        return String.join(" ", parts);
    }

    public String getConstraintSql() {
        if (!referencesTable) return null;
        if (referencedTable == null) {
            throw new IllegalStateException(String.format("Foreign key %s is referencesTable but has no referenced table", columnName));
        }
        return String.format(
            "CONSTRAINT fk_%s_%s FOREIGN KEY (%s) REFERENCES %s (%s) ON UPDATE %s ON DELETE %s",
            owningTable, columnName, columnName, referencedTable, referencedColumn, onUpdate, onDelete
        );
    }
}
```

#### AFTER
```java
package me.jobayeralmahmud.library.migrations;

import java.util.ArrayList;

public class ForeignKeyDefinition {
    public enum KeyType {
        INTEGER("BIGINT UNSIGNED"),
        UUID("BINARY(16)");

        private final String sqlType;

        KeyType(String sqlType) {
            this.sqlType = sqlType;
        }
    }

    private String owningTable;
    private final String columnName;
    private final KeyType keyType;

    private boolean nullable = false;
    private boolean unique = false;
    private String afterColumn;
    private Object defaultValue;

    private boolean referencesTable = false;
    private String referencedTable = null;
    private String referencedColumn = "id";
    private String onUpdate = "CASCADE";
    private String onDelete = "RESTRICT";

    ForeignKeyDefinition(String columnName, KeyType keyType) {
        this.columnName = columnName;
        this.keyType = keyType;
    }

    public void owningTable(String tableName) { this.owningTable = tableName; }
    public ForeignKeyDefinition nullable() { this.nullable = true; return this; }
    public ForeignKeyDefinition unique() { this.unique = true; return this; }
    public ForeignKeyDefinition after(String columnName) { this.afterColumn = columnName; return this; }
    public ForeignKeyDefinition defaultValue(Object defaultValue) { this.defaultValue = defaultValue; return this; }
    public ForeignKeyDefinition referencesTable(String table) { this.referencesTable = true; this.referencedTable = table; return this; }
    public ForeignKeyDefinition referencesColumn(String column) { this.referencedColumn = column; return this; }
    public ForeignKeyDefinition onDeleteCascade() { this.onDelete = "CASCADE"; return this; }
    public ForeignKeyDefinition onDeleteSetNull() { this.onDelete = "SET NULL"; return this; }
    public ForeignKeyDefinition onDeleteRestrict() { this.onDelete = "RESTRICT"; return this; }
    public ForeignKeyDefinition onUpdateCascade() { this.onUpdate = "CASCADE"; return this; }
    public ForeignKeyDefinition onUpdateSetNull() { this.onUpdate = "SET NULL"; return this; }
    public ForeignKeyDefinition onUpdateRestrict() { this.onUpdate = "RESTRICT"; return this; }

    public String getAfterColumn() { return afterColumn; }
    public String getColumnName() { return columnName; }
    public KeyType getKeyType() { return keyType; }
    public String getOwningTable() { return owningTable; }
    public boolean isNullable() { return nullable; }
    public boolean isUnique() { return unique; }
    public Object getDefaultValue() { return defaultValue; }
    public boolean isReferencesTable() { return referencesTable; }
    public String getReferencedTable() { return referencedTable; }
    public String getReferencedColumn() { return referencedColumn; }
    public String getOnUpdate() { return onUpdate; }
    public String getOnDelete() { return onDelete; }
}
```
