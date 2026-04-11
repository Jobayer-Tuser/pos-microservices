package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S2__CreateStoreAddressTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_store_address", table -> {
            table.uuid();
            table.foreignUuid("store_id").referencesTable("pos_store");
            table.string("address_line1");
            table.string("address_line2").nullable();
            table.string("city");
            table.string("state").nullable();
            table.string("postal_code").nullable();
            table.string("country");
            table.string("latitude").nullable();
            table.string("longitude").nullable();
            table.bool("is_primary").defaultValue(false);
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_store_address");
    }
}