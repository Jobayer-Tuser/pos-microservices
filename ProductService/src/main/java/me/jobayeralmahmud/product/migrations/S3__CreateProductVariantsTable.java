package me.jobayeralmahmud.product.migrations;

import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import me.jobayeralmahmud.product.enums.ProductStatus;
import org.springframework.stereotype.Component;

@Component
public class S3__CreateProductVariantsTable extends BaseMigration {

    @Override
    public void up(Schema schema) {
        schema.create("pos_product_variants", table -> {
            table.uuid();
            table.foreignUuid("product_id")
                    .referencesTable("pos_products")
                    .onDeleteCascade().onUpdateCascade();
            table.string("sku").unique();
            table.string("variant_name");
            table.string("variant_value");
            table.decimal("price");
            table.decimal("sell_price");
            table.integer("stock_quantity");
            table.enumeration("status", ProductStatus.values())
                    .defaultValue(ProductStatus.IN_STOCK.name());
        });
    }
}