package me.jobayeralmahmud.auth.database.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S3__CreatePermissionTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_permissions", table -> {
            table.id();
            table.string("name");
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_permissions");
    }
}