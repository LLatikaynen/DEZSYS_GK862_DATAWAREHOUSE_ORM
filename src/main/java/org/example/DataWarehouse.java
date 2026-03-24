package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class DataWarehouse {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String warehouseID;
    private String warehouseName;
    private String warehouseCity;

    @OneToMany(mappedBy = "dataWarehouse", cascade = CascadeType.ALL)
    private List<Product> products;

    public DataWarehouse() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getWarehouseID() { return warehouseID; }
    public void setWarehouseID(String warehouseID) { this.warehouseID = warehouseID; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseCity() { return warehouseCity; }
    public void setWarehouseCity(String warehouseCity) { this.warehouseCity = warehouseCity; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}

