package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Discount {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final DoubleProperty percentage;
    private final IntegerProperty storeId;

    public Discount(int id, int productId, double percentage, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.productId = new SimpleIntegerProperty(this, "Product Id", id);
        this.percentage = new SimpleDoubleProperty(this, "Percentage", percentage);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
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
}
