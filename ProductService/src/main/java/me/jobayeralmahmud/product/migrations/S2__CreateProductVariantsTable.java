package me.jobayeralmahmud.product.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S2__CreateProductVariantsTable extends BaseMigration {

    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_product_variants", table -> {
            table.id();
            table.foreignId("product_id").references("pos_products")
                    .onDeleteRestrict().onUpdateCascade();
            table.string("variant_name");
            table.string("variant_value");
            table.decimal("price", 12, 2);
            table.decimal("sell_price", 12, 2);
            table.integer("stock_quantity");
            table.string("status", 20).defaultValue("active");
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_product_variants");
    }
}