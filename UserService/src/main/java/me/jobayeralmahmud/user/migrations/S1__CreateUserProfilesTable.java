package me.jobayeralmahmud.user.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S1__CreateUserProfilesTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_user_profiles", table -> {
            table.id();
            table.foreignUuid("user_id");
            table.string("first_name").nullable();
            table.string("last_name").nullable();
            table.string("display_name").nullable();
            table.enumeration("gender", "Male", "Female");
            table.integer("age").nullable();
            table.string("phone_number").nullable();
            table.string("permanent_address").nullable();
            table.string("permanent_post_code").nullable();
            table.string("permanent_city").nullable();
            table.string("permanent_country").nullable();
            table.string("invoice_address").nullable();
            table.string("invoice_post_code").nullable();
            table.string("invoice_city").nullable();
            table.string("invoice_country").nullable();
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_user_profiles");
    }
}