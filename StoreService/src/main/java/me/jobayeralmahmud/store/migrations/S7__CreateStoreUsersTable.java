package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

@Component
public class S7__CreateStoreUsersTable extends BaseMigration {
    @Override
    public void up(Schema schema) {
        schema.create("pos_store_users", table -> {
            table.uuid();
            table.foreignUuid("store_id").referencesTable("pos_stores");
            table.uuid("user_id");
            table.uuid("role_id");
            table.enumeration("status", "ACTIVE", "INACTIVE").defaultValue("INACTIVE");
            table.timestamp("joined_at").defaultCurrentTimestamp();
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) {
        schema.dropIfExists("pos_store_users");
    }
}