package me.jobayeralmahmud.product.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

@Component
public class S4__CreateProductImagesTable extends BaseMigration {
    @Override
    public void up(Schema schema){
        schema.create("pos_product_images", table -> {
            table.uuid();
            table.foreignUuid("product_id").referencesTable("pos_products").onDeleteCascade();
            table.string("image_url", 500);
            table.bool("is_primary").defaultValue(false);
            table.integer("sort_order");
        });
    }
}