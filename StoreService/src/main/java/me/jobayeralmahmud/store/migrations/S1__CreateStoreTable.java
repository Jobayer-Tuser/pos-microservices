package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import me.jobayeralmahmud.store.enums.StoreStatus;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S1__CreateStoreTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_stores", table -> {
            table.uuid();
            table.foreignuuid("owner_id");
            table.string("brand_name");
            table.string("slug").unique();
            table.string("email").nullable();
            table.string("phone_number");
            table.string("logo_url").nullable();
            table.string("currency").nullable().defaultValue("BDT");
            table.string("timezone").nullable().defaultValue("Asia/Dhaka");
            table.bool("is_verified").defaultValue(false);
            table.string("description").nullable();
            table.string("verification_document_url").nullable();
            table.enumeration("status", StoreStatus.values()).defaultValue(StoreStatus.INACTIVE.name());
            table.timestamps();
            table.softDelete();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_stores");
    }
}