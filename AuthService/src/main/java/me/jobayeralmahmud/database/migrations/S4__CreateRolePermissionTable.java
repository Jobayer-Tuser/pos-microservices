package me.jobayeralmahmud.database.migrations;

import me.jobayeralmahmud.javamigrations.migrations.BaseMigration;
import me.jobayeralmahmud.javamigrations.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S4__CreateRolePermissionTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_role_permissions", table -> {
            table.foreignId("role_id").constrained("pos_roles");
            table.foreignId("permission_id").constrained("pos_permissions");
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_role_permissions");
    }
}