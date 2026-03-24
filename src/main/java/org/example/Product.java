package org.example;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String productID;
    private String productName;
    private int productQuantity;

    @ManyToOne
    @JoinColumn(name="warehouse_id")
    private DataWarehouse dataWarehouse;

    public Product() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getProductQuantity() { return productQuantity; }
    public void setProductQuantity(int productQuantity) { this.productQuantity = productQuantity; }
    public DataWarehouse getDataWarehouse() { return dataWarehouse; }
    public void setDataWarehouse(DataWarehouse dataWarehouse) { this.dataWarehouse = dataWarehouse; }
}