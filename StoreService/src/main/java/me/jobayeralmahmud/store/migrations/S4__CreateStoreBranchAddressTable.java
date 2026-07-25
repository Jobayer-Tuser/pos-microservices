package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

@Component
public class S4__CreateStoreBranchAddressTable extends BaseMigration {
    @Override
    public void up(Schema schema) {
        schema.create("pos_store_branch_addresses", table -> {
            table.uuid();
            table.foreignUuid("branch_id").referencesTable("pos_store_branches");
            table.string("address_line1");
            table.string("city");
            table.string("state").nullable();
            table.string("postal_code").nullable();
            table.string("country");
            table.string("latitude").nullable();
            table.string("longitude").nullable();
        });
    }

    @Override
    public void down(Schema schema) {
        schema.dropIfExists("pos_branches_address");
    }
}