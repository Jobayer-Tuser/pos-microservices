package me.jobayeralmahmud.library.migrations.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder {
/*
    private final ApplicationContext context;

    *//**
     * Register seeders to run here.
     *//*
    public void seed() {
        this.call(
                RolesSeeder.class,
                UserSeeder.class,
                ApartmentTypeSeeder.class,
                RoomTypeSeeder.class,
                BedTypeSeeder.class,
                FacilityCategorySeeder.class,
                FacilitySeeder.class
        );
    }

    @SafeVarargs
    public final void call(Class<? extends Seeder>... seederClasses) {
        for (Class<? extends Seeder> seederClass : seederClasses) {
            try {
                Seeder seeder = context.getBean(seederClass);
                seeder.run();
            } catch (Exception e) {
                log.error("Failed to run seeder {}: {}", seederClass.getSimpleName(), e.getMessage());
                throw e;
            }
        }
    }

    public void runMigration(String specificSeederName) {
        log.info("Starting database seeding...");
        try {
            if (specificSeederName != null && !specificSeederName.isEmpty()) {
                // Run specific seeder by name
                try {
                    Seeder seeder = (Seeder) context.getBean(specificSeederName);
                    seeder.run();
                } catch (NoSuchBeanDefinitionException e) {
                    String beanName = Character.toLowerCase(specificSeederName.charAt(0))
                            + specificSeederName.substring(1);
                    Seeder seeder = (Seeder) context.getBean(beanName);
                    seeder.run();
                }
            } else {
                seed();
            }
            log.info("Database seeding completed.");
        } catch (Exception e) {
            log.error("Error during database seeding: {}", e.getMessage());
        }
    }*/
}