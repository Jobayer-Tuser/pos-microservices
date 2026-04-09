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
            table.bigInteger("store_id").unsigned();
            table.foreignuuid("category_id")
                    .nullable().references("pos_product_categories")
                    .onUpdateCascade().onDeleteRestrict();
            table.string("name");
            table.string("sku").unique();
            table.text("description");
            table.text("image_url");
            table.string("brand", 64);
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_products");
    }
}