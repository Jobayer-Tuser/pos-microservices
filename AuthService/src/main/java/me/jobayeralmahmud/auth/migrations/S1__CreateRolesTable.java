package me.jobayeralmahmud.auth.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

@Component
public class S1__CreateRolesTable extends BaseMigration {

    @Override
    public void up(Schema schema) {
        schema.create("pos_roles", table -> {
            table.id();
            table.string("name", 32);
            table.timestamps();
        });
    }
}