package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

import java.util.Date;

public class Receipt {
    private final IntegerProperty id;
    private final IntegerProperty customerId;
    private final IntegerProperty userId;
    private final ObjectProperty<Date> date;
    private final DoubleProperty price;
    private final IntegerProperty storeId;

    public Receipt(IntegerProperty id, IntegerProperty customerId, IntegerProperty userId, ObjectProperty<Date> date, DoubleProperty price, IntegerProperty storeId) {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.date = date;
        this.price = price;
        this.storeId = storeId;
    }
}
