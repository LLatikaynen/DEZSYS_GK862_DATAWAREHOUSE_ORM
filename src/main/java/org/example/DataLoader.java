package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final DataWarehouseRepository warehouseRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;

    public DataLoader(DataWarehouseRepository warehouseRepository,
                      PurchaseRepository purchaseRepository,
                      ProductRepository productRepository) {
        this.warehouseRepository = warehouseRepository;
        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (warehouseRepository.count() == 0) {
            System.out.println("LADE TESTDATEN IN MYSQL ");

            DataWarehouse w1 = new DataWarehouse();
            w1.setWarehouseID("001");
            w1.setWarehouseName("Linz Bahnhof");
            w1.setWarehouseCity("Linz");

            Product p1 = createProduct("00-443175", "Orangensaft", 2500, w1);
            Product p2 = createProduct("00-871895", "Apfelsaft", 3420, w1);
            Product p3 = createProduct("00-111111", "Wasser", 5000, w1);
            Product p4 = createProduct("00-222222", "Cola", 1200, w1);
            Product p5 = createProduct("00-333333", "Eistee", 800, w1);
            w1.setProducts(Arrays.asList(p1, p2, p3, p4, p5));
            warehouseRepository.save(w1);

            DataWarehouse w2 = new DataWarehouse();
            w2.setWarehouseID("002");
            w2.setWarehouseName("Wien Zentrum");
            w2.setWarehouseCity("Wien");

            Product p6 = createProduct("00-444444", "Kaffee", 300, w2);
            Product p7 = createProduct("00-555555", "Milch", 1500, w2);
            Product p8 = createProduct("00-666666", "Zucker", 400, w2);
            Product p9 = createProduct("00-777777", "Mehl", 600, w2);
            Product p10 = createProduct("00-888888", "Schoko", 1200, w2);
            w2.setProducts(Arrays.asList(p6, p7, p8, p9, p10));
            warehouseRepository.save(w2);

            List<Product> allProducts = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);

            int purchaseCounter = 1;
            for (Product prod : allProducts) {
                for (int i = 0; i < 3; i++) {
                    Purchase purchase = new Purchase(
                            LocalDateTime.now().minusDays(purchaseCounter),
                            (int)(Math.random() * 50) + 1,
                            prod.getDataWarehouse().getWarehouseCity(),
                            prod,
                            prod.getDataWarehouse()
                    );
                    purchaseRepository.save(purchase);
                    purchaseCounter++;
                }
            }

            System.out.println("2 WAREHOUSES, 10 PRODUKTE UND " + (purchaseCounter - 1) + " PURCHASES GELADEN ");

            System.out.println("TEST: Suche Warehouse 001: " + warehouseRepository.findByWarehouseID("001").get().getWarehouseName());
            System.out.println("TEST: Suche Produkt 00-444444 in Warehouse 002: " +
                    productRepository.findByProductIDAndDataWarehouse_WarehouseID("00-444444", "002").get().getProductName());
        }
    }

    private Product createProduct(String pID, String name, int qty, DataWarehouse w) {
        Product p = new Product();
        p.setProductID(pID);
        p.setProductName(name);
        p.setProductQuantity(qty);
        p.setDataWarehouse(w);
        return p;
    }
}