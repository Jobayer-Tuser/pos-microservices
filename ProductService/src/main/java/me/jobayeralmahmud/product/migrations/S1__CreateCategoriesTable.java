package me.jobayeralmahmud.product.migrations;


import me.jobayeralmahmud.dbmigration.api.BaseMigration;
import me.jobayeralmahmud.dbmigration.schema.Schema;
import org.springframework.stereotype.Component;

@Component
public class S1__CreateCategoriesTable extends BaseMigration {
    @Override
    public void up(Schema schema) {
        schema.create("pos_product_categories", table -> {
            table.uuid();
            table.foreignUuid("parent_id").nullable()
                    .referencesTable("pos_product_categories").onDeleteSetNull();
            table.string("name");
            table.text("description");
            table.string("slug").unique();
        });
    }
}