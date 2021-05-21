package com.zuzka.myshowcode.model;

import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int quantityInStock;
    private double pricePerUnit;

    public Product() {
    }

    public Product(Long id, String name, int quantityInStock, double pricePerUnit) {
        this.id = id;
        this.name = name;
        this.quantityInStock = quantityInStock;
        this.pricePerUnit = pricePerUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
