package org.example;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Optional<Product> findByProductIDAndDataWarehouse_WarehouseID(String productID, String warehouseID);

}