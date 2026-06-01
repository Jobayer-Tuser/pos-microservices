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
