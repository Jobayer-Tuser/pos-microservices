package me.jobayeralmahmud.database.migrations;

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
            table.foreignuuid("user_id")
                    .constrained("pos_users").onUpdateCascade().onDeleteRestrict();
            table.string("token");
            table.string("token_type", 32);
            table.datetime("created_at");
            table.datetime("expired_at");
            table.datetime("verified_at").nullable();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_verification_tokens");
    }
}