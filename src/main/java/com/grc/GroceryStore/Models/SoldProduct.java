package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public class SoldProduct {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final IntegerProperty receiptId;
    private final DoubleProperty price;
    private final DoubleProperty paidMoney;
    private final DoubleProperty paidPoints;
    private final DoubleProperty discount;
    private final IntegerProperty storeId;

    public SoldProduct(IntegerProperty id, IntegerProperty productId, IntegerProperty receiptId, DoubleProperty price, DoubleProperty paidMoney, DoubleProperty paidPoints, DoubleProperty discount, IntegerProperty storeId) {
        this.id = id;
        this.productId = productId;
        this.receiptId = receiptId;
        this.price = price;
        this.paidMoney = paidMoney;
        this.paidPoints = paidPoints;
        this.discount = discount;
        this.storeId = storeId;
    }
}
