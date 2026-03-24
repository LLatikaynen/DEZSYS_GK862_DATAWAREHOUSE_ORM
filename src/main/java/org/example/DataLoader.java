package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final DataWarehouseRepository warehouseRepository;

    public DataLoader(DataWarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (warehouseRepository.count() == 0) {
            System.out.println("load data");

            DataWarehouse w1 = new DataWarehouse();
            w1.setWarehouseID("001");
            w1.setWarehouseName("Linz Bahnhof");
            w1.setWarehouseCity("Linz");

            Product p1 = new Product(); p1.setProductID("00-443175"); p1.setProductName("Orangensaft"); p1.setProductQuantity(2500); p1.setDataWarehouse(w1);
            Product p2 = new Product(); p2.setProductID("00-871895"); p2.setProductName("Apfelsaft"); p2.setProductQuantity(3420); p2.setDataWarehouse(w1);

            w1.setProducts(Arrays.asList(p1, p2));
            warehouseRepository.save(w1);

            System.out.println("data saved");
        }
    }
}