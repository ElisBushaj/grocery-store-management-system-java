package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final IntegerProperty id;
    private final IntegerProperty categoryId;
    private final StringProperty name;
    private final StringProperty description;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final StringProperty suplierInfo;
    private final IntegerProperty storeId;

    public Product(IntegerProperty id, IntegerProperty categoryId, StringProperty name, StringProperty description, DoubleProperty price, IntegerProperty stock, StringProperty suplierInfo, IntegerProperty storeId) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.suplierInfo = suplierInfo;
        this.storeId = storeId;
    }
}
