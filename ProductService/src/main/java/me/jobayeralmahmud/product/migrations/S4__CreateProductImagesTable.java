package me.jobayeralmahmud.product.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S4__CreateProductImagesTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_product_images", table -> {
            table.id();
            table.foreignId("product_id").references("pos_products").onDeleteCascade();
            table.string("image_url", 500);
            table.bool("is_primary").defaultValue(false);
            table.integer("sort_order");
        });

    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_product_images");
    }
}