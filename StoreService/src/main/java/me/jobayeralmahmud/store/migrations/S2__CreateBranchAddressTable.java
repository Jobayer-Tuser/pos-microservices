package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S2__CreateBranchAddressTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_branches_address", table -> {
            table.uuid();
            table.uuidForeign("branch_id").constrained("pos_store_branches");
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
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_branches_address");
    }
}