package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

public class Discount {
    private final IntegerProperty id;
    private final ObjectProperty<Product> product;
    private final DoubleProperty percentage;
    private final IntegerProperty storeId;

    public Discount(int id, Product product, double percentage, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.product = new SimpleObjectProperty<>(this, "Product", product);
        this.percentage = new SimpleDoubleProperty(this, "Percentage", percentage);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public double getPercentage() {
        return percentage.get();
    }

    public DoubleProperty percentageProperty() {
        return percentage;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }

    public Product getProduct() {
        return product.get();
    }

    public ObjectProperty<Product> productProperty() {
        return product;
    }


}
