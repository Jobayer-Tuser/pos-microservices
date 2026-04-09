package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S4__CreateStoreUsersTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_store_users", table -> {
            table.uuid();
            table.foreignuuid("store_id").constrained("pos_stores");
            table.uuid("user_id");
            table.uuid("role_id");
            table.enumeration("status", "ACTIVE", "INACTIVE").defaultValue("INACTIVE");
            table.timeStamp("joined_at").defaultCurrentTimestamp();
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_store_users");
    }
}