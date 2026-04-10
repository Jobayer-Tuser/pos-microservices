package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S5__CreateStoreSettingsTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_store_settings", table -> {
            table.uuid();
            table.uuidForeign("store_id").references("pos_store");
            table.string("settings_key").unique();
            table.json("settings_value");
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_store_settings");
    }
}