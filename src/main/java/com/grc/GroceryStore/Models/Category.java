package com.grc.GroceryStore.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Category {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty storeId;

    public Category(IntegerProperty id, StringProperty name, IntegerProperty storeId) {
        this.id = id;
        this.name = name;
        this.storeId = storeId;
    }
}
