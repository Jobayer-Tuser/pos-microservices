package me.jobayeralmahmud.auth.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

@Component
public class S4__CreateRolePermissionTable extends BaseMigration {

    @Override
    public void up(Schema schema) {
        schema.create("pos_role_permissions", table -> {
            table.foreignId("role_id").referencesTable("pos_roles");
            table.foreignId("permission_id").referencesTable("pos_permissions");
        });
    }
}