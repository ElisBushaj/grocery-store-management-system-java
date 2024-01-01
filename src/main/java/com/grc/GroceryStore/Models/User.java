package com.grc.GroceryStore.Models;

import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class User {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty lastname;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty role;
    private final IntegerProperty storeId;

    public User(IntegerProperty id, StringProperty name, StringProperty lastname, StringProperty email, StringProperty password, StringProperty role, IntegerProperty storeId) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.storeId = storeId;
    }
}
