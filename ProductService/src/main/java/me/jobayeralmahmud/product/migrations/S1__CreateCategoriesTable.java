package me.jobayeralmahmud.product.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S1__CreateCategoriesTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_product_categories", table -> {
            table.uuid();
            table.uuidForeign("parent_id").nullable()
                    .constrained("pos_product_categories").onDeleteSetNull();
            table.string("name");
            table.text("description");
            table.string("slug").unique();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_product_categories");
    }
}