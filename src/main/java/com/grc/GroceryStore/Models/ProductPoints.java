package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ProductPoints {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final IntegerProperty priceInPoints;
    private final IntegerProperty storeId;


    public ProductPoints(int id, int productId, int priceInPoints, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.productId = new SimpleIntegerProperty(this, "Product Id", productId);
        this.priceInPoints = new SimpleIntegerProperty(this, "Price In Points", priceInPoints);
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

    public int getPriceInPoints() {
        return priceInPoints.get();
    }

    public IntegerProperty priceInPointsProperty() {
        return priceInPoints;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }
}
