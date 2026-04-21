package org.example;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDateTime purchaseDate;
    private int amount;
    private String location;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private DataWarehouse dataWarehouse;

    public Purchase() {}

    public Purchase(LocalDateTime purchaseDate, int amount, String location, Product product, DataWarehouse dataWarehouse) {
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.location = location;
        this.product = product;
        this.dataWarehouse = dataWarehouse;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public DataWarehouse getDataWarehouse() { return dataWarehouse; }
    public void setDataWarehouse(DataWarehouse dataWarehouse) { this.dataWarehouse = dataWarehouse; }
}