package me.jobayeralmahmud.database.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S6__CreateJwtTokenTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_jwt_tokens", table -> {
            table.id();
            table.foreignuuid("user_id").constrained("pos_users").onDeleteRestrict().onUpdateCascade();
            table.string("token");
            table.bool("is_logged_out").defaultValue(false);
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_jwt_tokens");
    }
}