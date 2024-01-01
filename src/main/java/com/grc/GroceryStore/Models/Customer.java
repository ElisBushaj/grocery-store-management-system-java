package com.grc.GroceryStore.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty lastname;
    private final StringProperty phoneNumber;
    private final StringProperty gender;
    private final IntegerProperty points;
    private final IntegerProperty storeId;

    public Customer(IntegerProperty id, StringProperty name, StringProperty lastname, StringProperty phoneNumber, StringProperty gender, IntegerProperty points, IntegerProperty storeId) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.points = points;
        this.storeId = storeId;
    }
}
