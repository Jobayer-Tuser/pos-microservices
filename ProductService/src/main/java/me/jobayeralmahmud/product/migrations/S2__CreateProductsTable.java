package me.jobayeralmahmud.product.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S2__CreateProductsTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_products", table -> {
            table.uuid();
            table.uuid("store_id");
            table.uuidForeign("category_id").nullable()
                    .constrained("pos_product_categories").onUpdateCascade().onDeleteSetNull();
            table.string("name");
            table.string("slug").unique();
            table.text("description").nullable();
            table.text("image_url").nullable();
            table.string("brand", 64).nullable();
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_products");
    }
}