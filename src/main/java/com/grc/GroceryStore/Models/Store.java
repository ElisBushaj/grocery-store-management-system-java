package com.grc.GroceryStore.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

public class Store {
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty pointsPerEuro;

    public Store(IntegerProperty id, StringProperty name, DoubleProperty pointsPerEuro) {
        this.id = id;
        this.name = name;
        this.pointsPerEuro = pointsPerEuro;
    }
}
