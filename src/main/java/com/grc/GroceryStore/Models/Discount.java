package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
public class Discount {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final DoubleProperty percentage;
    private final IntegerProperty storeId;

    public Discount(IntegerProperty id, IntegerProperty productId, DoubleProperty percentage, IntegerProperty storeId) {
        this.id = id;
        this.productId = productId;
        this.percentage = percentage;
        this.storeId = storeId;
    }
}
