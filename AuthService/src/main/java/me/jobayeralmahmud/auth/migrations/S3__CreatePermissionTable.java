package me.jobayeralmahmud.auth.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

@Component
public class S3__CreatePermissionTable extends BaseMigration {

    @Override
    public void up(Schema schema) {
        schema.create("pos_permissions", table -> {
            table.id();
            table.string("name");
        });
    }
}