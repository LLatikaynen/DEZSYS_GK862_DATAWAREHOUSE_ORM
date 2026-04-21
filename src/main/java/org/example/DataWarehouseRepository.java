package org.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface DataWarehouseRepository extends CrudRepository<DataWarehouse, Integer> {

    Optional<DataWarehouse> findByWarehouseID(String warehouseID);

    @Modifying
    @Transactional
    @Query("UPDATE DataWarehouse d SET d.warehouseName = :newName WHERE d.warehouseID = :warehouseID")
    int updateWarehouseName(String warehouseID, String newName);

}