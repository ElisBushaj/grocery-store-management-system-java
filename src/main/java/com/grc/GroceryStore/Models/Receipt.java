package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

import java.util.Date;

public class Receipt {
    private final IntegerProperty id;
    private final IntegerProperty customerId;
    private final IntegerProperty userId;
    private final ObjectProperty<Date> date;
    private final DoubleProperty price;
    private final IntegerProperty storeId;

    public Receipt(int id, int customerId, int userId, Date date, double price, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.customerId = new SimpleIntegerProperty(this, "Customer Id", customerId);;
        this.userId = new SimpleIntegerProperty(this, "User Id", userId);;
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.price = new SimpleDoubleProperty(this, "Price", price);;
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getCustomerId() {
        return customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }
}
