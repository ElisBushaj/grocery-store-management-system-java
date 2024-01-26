package com.grc.GroceryStore.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SoldProduct {
    private final IntegerProperty id;
    private final IntegerProperty productId;
    private final IntegerProperty receiptId;
    private final DoubleProperty price;
    private final DoubleProperty paidMoney;
    private final DoubleProperty paidPoints;
    private final DoubleProperty discount;
    private final IntegerProperty storeId;

    public SoldProduct(int id, int productId, int receiptId, double price, double paidMoney, double paidPoints, double discount, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.productId = new SimpleIntegerProperty(this, "Product Id", productId);
        this.receiptId = new SimpleIntegerProperty(this, "Receipt Id", receiptId);
        this.price = new SimpleDoubleProperty(this, "Price", price);
        this.paidMoney = new SimpleDoubleProperty(this, "Paid Money", paidMoney);
        this.paidPoints = new SimpleDoubleProperty(this, "Paid Points", paidPoints);
        this.discount = new SimpleDoubleProperty(this, "Discount", discount);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public int getReceiptId() {
        return receiptId.get();
    }

    public IntegerProperty receiptIdProperty() {
        return receiptId;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public double getPaidMoney() {
        return paidMoney.get();
    }

    public DoubleProperty paidMoneyProperty() {
        return paidMoney;
    }

    public double getPaidPoints() {
        return paidPoints.get();
    }

    public DoubleProperty paidPointsProperty() {
        return paidPoints;
    }

    public double getDiscount() {
        return discount.get();
    }

    public DoubleProperty discountProperty() {
        return discount;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }

    public static ArrayList<SoldProduct> getSoldProductsFromDB() {
        ArrayList<SoldProduct> soldProducts = new ArrayList<>();

        try {
            String query = "SELECT * FROM SoldProduct WHERE storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, Model.getInstance().getStore().getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("productId");
                int receiptId = resultSet.getInt("receiptId");
                double price = resultSet.getDouble("price");
                double paidMoney = resultSet.getDouble("paidMoney");
                double paidPoints = resultSet.getDouble("paidPoints");
                double discount = resultSet.getDouble("discount");
                int storeId = resultSet.getInt("storeId");

                SoldProduct soldProduct = new SoldProduct(id, productId, receiptId, price, paidMoney, paidPoints, discount, storeId);
                soldProducts.add(soldProduct);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soldProducts;
    }
}
