package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SellProduct extends Product{
    private final ObjectProperty<Discount> discount;
    private final ObjectProperty<ProductPoints> points;
    private final DoubleProperty priceAfterDiscount;
    public SellProduct(int id, Category category, String name, String description, double price, int stock, String supplierInfo, int storeId, Discount discount, ProductPoints points) {
        super(id, category, name, description, price, stock, supplierInfo, storeId);

        this.discount = new SimpleObjectProperty<>(this,"Discount", discount);
        this.points = new SimpleObjectProperty<>(this, "Product Points", points);
        double priceAfterDiscount = price;

        if(discount != null){
            double percentage = discount.getPercentage();
            priceAfterDiscount = price - price * percentage;
        }

        this.priceAfterDiscount = new SimpleDoubleProperty(priceAfterDiscount);
    }

    public SellProduct(Product product, Discount discount, ProductPoints points){
        this(product.getId(), product.getCategory(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getSupplierInfo(), product.getStoreId(), discount, points);
    }


    public Discount getDiscount() {
        return discount.get();
    }

    public ObjectProperty<Discount> discountProperty() {
        return discount;
    }

    public ProductPoints getPoints() {
        return points.get();
    }

    public ObjectProperty<ProductPoints> pointsProperty() {
        return points;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount.get();
    }

    public DoubleProperty priceAfterDiscountProperty() {
        return priceAfterDiscount;
    }
}
