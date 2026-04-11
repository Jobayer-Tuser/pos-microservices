package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import me.jobayeralmahmud.store.enums.StoreStatus;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S3__CreateStoreBranchesTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_store_branches", table -> {
            table.uuid();
            table.foreignUuid("store_id").referencesTable("pos_stores");
            table.string("name");
            table.string("code").unique();
            table.string("phone_number");
            table.string("email").nullable();
            table.enumeration("status", StoreStatus.values()).defaultValue(StoreStatus.INACTIVE.name());
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_store_address");
    }
}