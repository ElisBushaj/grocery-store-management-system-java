package com.grc.GroceryStore.Models;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleIntegerProperty;

public class CartProduct extends SellProduct {
    private final IntegerProperty QTY = new SimpleIntegerProperty(1);
    private final DoubleProperty total;

    public CartProduct(int id, Category category, String name, String description, double price, int stock, String supplierInfo, int storeId, Discount discount, ProductPoints points) {
        super(id, category, name, description, price, stock, supplierInfo, storeId, discount, points);
        this.total = new ReadOnlyDoubleWrapper();
        this.total.bind(Bindings.multiply(QTY, getPriceAfterDiscount()));
    }

    public CartProduct(Product product, Discount discount, ProductPoints points) {
        super(product, discount, points);
        this.total = new ReadOnlyDoubleWrapper();
        this.total.bind(Bindings.multiply(QTY, getPriceAfterDiscount()));
    }

    public CartProduct(SellProduct sellProduct) {
        super(sellProduct.getId(), sellProduct.getCategory(), sellProduct.getName(), sellProduct.getDescription(), sellProduct.getPrice(), sellProduct.getStock(), sellProduct.getSupplierInfo(), sellProduct.getStoreId(), sellProduct.getDiscount(), sellProduct.getPoints());
        this.total = new ReadOnlyDoubleWrapper();
        this.total.bind(Bindings.multiply(QTY, getPriceAfterDiscount()));
    }

    public int getQTY() {
        return QTY.get();
    }

    public IntegerProperty QTYProperty() {
        return QTY;
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
