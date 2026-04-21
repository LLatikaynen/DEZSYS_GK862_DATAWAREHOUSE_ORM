package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final DataWarehouseRepository warehouseRepo;
    private final ProductRepository productRepo;
    private final PurchaseRepository purchaseRepo;

    public DatabaseSeeder(DataWarehouseRepository warehouseRepo, ProductRepository productRepo, PurchaseRepository purchaseRepo) {
        this.warehouseRepo = warehouseRepo;
        this.productRepo = productRepo;
        this.purchaseRepo = purchaseRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        long purchaseCount = purchaseRepo.count();
        System.out.println("DatabaseSeeder: Gefundene Purchases = " + purchaseCount);

        // Nur ausführen wenn DataLoader bereits 30 Purchases erstellt hat
        if (purchaseCount == 30) {
            System.out.println("=== STARTE 300 PURCHASE GENERIERUNG ===");
            Random random = new Random();

            List<DataWarehouse> warehouses = new ArrayList<>((List<DataWarehouse>) warehouseRepo.findAll());
            List<Product> products = new ArrayList<>((List<Product>) productRepo.findAll());

            System.out.println("Warehouses: " + warehouses.size() + ", Produkte: " + products.size());

            int generiert = 0;
            for (int i = 0; i < 300; i++) {
                if (warehouses.isEmpty() || products.isEmpty()) {
                    System.out.println("FEHLER: Keine Warehouses oder Produkte vorhanden!");
                    break;
                }

                Product randomProduct = products.get(random.nextInt(products.size()));
                DataWarehouse randomWarehouse = warehouses.get(random.nextInt(warehouses.size()));
                LocalDateTime randomDate = LocalDateTime.now().minusDays(random.nextInt(180));

                Purchase purchase = new Purchase();
                purchase.setProduct(randomProduct);
                purchase.setDataWarehouse(randomWarehouse);
                purchase.setAmount(random.nextInt(50) + 1);
                purchase.setPurchaseDate(randomDate);
                purchase.setLocation(randomWarehouse.getWarehouseCity());

                purchaseRepo.save(purchase);
                generiert++;

                if ((i + 1) % 100 == 0) {
                    System.out.println("  ... " + (i + 1) + " Purchases generiert");
                }
            }
            System.out.println("✓ FERTIG: " + generiert + " Datensätze generiert!");
        } else {
            System.out.println("DatabaseSeeder: Übersprungen (erwartet 30 Purchases, gefunden " + purchaseCount + ")");
        }
    }
}
