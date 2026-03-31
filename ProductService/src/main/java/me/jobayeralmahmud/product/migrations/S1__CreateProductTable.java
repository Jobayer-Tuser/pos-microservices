package me.jobayeralmahmud.product.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S1__CreateProductTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.table("pos_products", table -> {
            table.id();
            table.bigInteger("store_id");
            table.foreignId("category_id")
                    .references("pos_product_categories").onDeleteSetNull();
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