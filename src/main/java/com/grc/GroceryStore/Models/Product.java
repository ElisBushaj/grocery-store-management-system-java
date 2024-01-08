package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty id;
    private final IntegerProperty categoryId;
    private final StringProperty name;
    private final StringProperty description;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final StringProperty supplierInfo;
    private final IntegerProperty storeId;

    public Product(int id, int categoryId, String name, String description, double price, int stock, String supplierInfo, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.categoryId = new SimpleIntegerProperty(this, "Category Id", categoryId);
        this.name = new SimpleStringProperty(this, "Name", name);
        this.description = new SimpleStringProperty(this, "Description", description);
        this.price = new SimpleDoubleProperty(this, "Price", price);
        this.stock = new SimpleIntegerProperty(this, "Stock", stock);
        this.supplierInfo = new SimpleStringProperty(this, "Supplier Info", supplierInfo);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getCategoryId() {
        return categoryId.get();
    }

    public IntegerProperty categoryIdProperty() {
        return categoryId;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public int getStock() {
        return stock.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public String getSupplierInfo() {
        return supplierInfo.get();
    }

    public StringProperty supplierInfoProperty() {
        return supplierInfo;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }
}
