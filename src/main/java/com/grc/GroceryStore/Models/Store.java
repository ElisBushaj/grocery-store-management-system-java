package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

public class Store {
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty pointsPerEuro;

    public Store(int id, String name, double pointsPerEuro) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.name = new SimpleStringProperty(this, "Name", name);
        this.pointsPerEuro = new SimpleDoubleProperty(this, "Points Per Euro", pointsPerEuro);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getPointsPerEuro() {
        return pointsPerEuro.get();
    }

    public DoubleProperty pointsPerEuroProperty() {
        return pointsPerEuro;
    }
}
