package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public class ProductPoints {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final IntegerProperty priceInPoints;
    private final IntegerProperty storeId;


    public ProductPoints(IntegerProperty id, IntegerProperty productId, IntegerProperty priceInPoints, IntegerProperty storeId) {
        this.id = id;
        this.productId = productId;
        this.priceInPoints = priceInPoints;
        this.storeId = storeId;
    }
}
