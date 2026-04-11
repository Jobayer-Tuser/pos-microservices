package me.jobayeralmahmud.auth.database.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S5__CreateVerificationTokenTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_verification_token", table -> {
            table.id();
            table.foreignUuid("user_id").referencesTable("pos_users").onUpdateCascade().onDeleteRestrict();
            table.string("token");
            table.string("token_type", 32);
            table.dateTime("created_at");
            table.dateTime("expired_at");
            table.dateTime("verified_at").nullable();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_verification_tokens");
    }
}