package me.jobayeralmahmud.auth.database.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;
import java.sql.SQLException;

@Component
public class S2__CreateUsersTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_users", table -> {
            table.uuid();
            table.foreignId("role_id").nullable().defaultValue(1).constrained("pos_roles").onUpdateCascade().onDeleteRestrict();
            table.string("email").unique();
            table.string("username");
            table.string("password");
            table.integer("is_active").nullable();
            table.dateTime("email_verified_at").nullable();
            table.integer("created_by").nullable();
            table.integer("updated_by").nullable();
            table.dateTime("deleted_at").nullable();
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_users");
    }
}