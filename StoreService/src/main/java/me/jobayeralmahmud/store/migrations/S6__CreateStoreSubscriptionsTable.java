package me.jobayeralmahmud.store.migrations;

import me.jobayeralmahmud.library.migrations.BaseMigration;
import me.jobayeralmahmud.library.migrations.Schema;
import me.jobayeralmahmud.store.enums.BillingCycle;
import me.jobayeralmahmud.store.enums.PaymentMethod;
import me.jobayeralmahmud.store.enums.PaymentStatus;
import me.jobayeralmahmud.store.enums.SubscriptionStatus;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class S6__CreateStoreSubscriptionsTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {

        schema.create("pos_store_subscriptions", table -> {
            table.uuid();
            table.foreignuuid("store_id").references("pos_store");
            table.string("plan_name");
            table.doubleColumn("plan_price");
            table.enumeration("status", SubscriptionStatus.values()).defaultValue(SubscriptionStatus.ACTIVE.name());
            table.enumeration("payment_method", PaymentMethod.values()).defaultValue(PaymentMethod.CREDIT_CARD.name());
            table.enumeration("payment_status", PaymentStatus.values()).defaultValue(PaymentStatus.PENDING.name());
            table.enumeration("billing_cycle", BillingCycle.values()).defaultValue(BillingCycle.MONTHLY.name());
            table.timeStamp("started_at").defaultCurrentTimestamp();
            table.timeStamp("ended_at").nullable();
            table.timestamps();
        });
    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_store_subscriptions");
    }
}