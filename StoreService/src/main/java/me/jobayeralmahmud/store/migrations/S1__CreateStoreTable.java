package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S1__CreateStoreTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_store", table -> {
            table.id();
            table.foreignuuid("owner_id");
            table.string("brand_name");
            table.string("description").nullable();
            table.string("email").nullable();
            table.string("phone_number");
            table.string("address").nullable();
            table.string("store_type").nullable();
            table.enumeration("status", "ACTIVE", "INACTIVE", "CLOSED", "SUSPENDED")
                    .defaultValue("INACTIVE");
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_store");
    }
}
