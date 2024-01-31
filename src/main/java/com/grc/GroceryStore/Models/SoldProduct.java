package com.grc.GroceryStore.Models;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SoldProduct {
    private final IntegerProperty id;
    private final ObjectProperty<Product> product;
    private final IntegerProperty receiptId;
    private final DoubleProperty price;
    private final DoubleProperty paidMoney;
    private final DoubleProperty paidPoints;
    private final DoubleProperty discount;
    private final IntegerProperty storeId;

    public SoldProduct(int id, Product product, int receiptId, double price, double paidMoney, double paidPoints, double discount, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.product = new SimpleObjectProperty<>(this, "Product ", product);
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

                Product product = Model.getInstance().getStore().findProductById(productId);
                if(product == null){
                    continue;
                }

                SoldProduct soldProduct = new SoldProduct(id, product, receiptId, price, paidMoney, paidPoints, discount, storeId);
                soldProducts.add(soldProduct);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soldProducts;
    }
    public static ArrayList<SoldProduct> getSoldProductsByReceiptIdFromDB(int receiptId) {
        ArrayList<SoldProduct> soldProducts = new ArrayList<>();

        try {
            String query = "SELECT * FROM SoldProduct WHERE receiptId = ? AND storeId=?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, receiptId);
            statement.setInt(2,Model.getInstance().getStore().getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("productId");
                double price = resultSet.getDouble("price");
                double paidMoney = resultSet.getDouble("paidMoney");
                double paidPoints = resultSet.getDouble("paidPoints");
                double discount = resultSet.getDouble("discount");

                Product product = Model.getInstance().getStore().findProductById(productId);
                if (product == null) {
                    continue;
                }

                SoldProduct soldProduct = new SoldProduct(id, product, receiptId, price, paidMoney, paidPoints, discount, Model.getInstance().getStore().getId());
                soldProducts.add(soldProduct);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return soldProducts;
    }

    public static boolean createSoldProductsCashDB(ObservableList<CartProduct> cartProducts, int receiptId) {
        if (cartProducts == null || cartProducts.isEmpty()) {
            return false;  // Nothing to insert
        }

        try {
            String query = "INSERT INTO SoldProduct (productId, receiptId, price, paidMoney, paidPoints, discount, storeId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String removeStockQuery = "UPDATE Product SET stock = ? WHERE id = ?";

            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            PreparedStatement removeStockStatement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(removeStockQuery);

            for (CartProduct cartProduct : cartProducts) {
                for(int i = 0; i < cartProduct.getQTY(); i++) {
                    statement.setInt(1, cartProduct.getId());
                    statement.setInt(2, receiptId);
                    statement.setDouble(3, cartProduct.getPrice());
                    statement.setDouble(4, cartProduct.getPriceAfterDiscount());
                    statement.setDouble(5, 0);
                    if (cartProduct.getDiscount() == null) {
                        statement.setDouble(6, 0);
                    } else {
                        statement.setDouble(6, cartProduct.getDiscount().getPercentage());
                    }
                    statement.setInt(7, cartProduct.getStoreId());
                    statement.addBatch();

                    removeStockStatement.setInt(1, cartProduct.getStock());
                    removeStockStatement.setInt(2, cartProduct.getId());
                    removeStockStatement.addBatch();
                }
            }
            int[] rowsAffectedStock = removeStockStatement.executeBatch();
            removeStockStatement.close();

            // Check if all batches were successful
            for (int row : rowsAffectedStock) {
                if (row <= 0) {
                    return false;
                }
            }

            int[] rowsAffected = statement.executeBatch();
            statement.close();

            // Check if all batches were successful
            for (int row : rowsAffected) {
                if (row <= 0) {
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createSoldProductsCashAndPointsDB(ObservableList<CartProduct> cartProducts, int receiptId) {
        if (cartProducts == null || cartProducts.isEmpty()) {
            return false;  // Nothing to insert
        }

        try {
            String query = "INSERT INTO SoldProduct (productId, receiptId, price, paidMoney, paidPoints, discount, storeId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String removeStockQuery = "UPDATE Product SET stock = ? WHERE id = ?";

            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            PreparedStatement removeStockStatement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(removeStockQuery);

            for (CartProduct cartProduct : cartProducts) {
                for(int i = 0; i < cartProduct.getQTY(); i++) {
                    statement.setInt(1, cartProduct.getId());
                    statement.setInt(2, receiptId);
                    statement.setDouble(3, cartProduct.getPrice());

                    if (cartProduct.getPoints() == null) {
                        statement.setDouble(4, cartProduct.getPriceAfterDiscount());
                        statement.setDouble(5, 0);
                    } else {
                        statement.setDouble(4, 0);
                        statement.setDouble(5, cartProduct.getPoints().getPriceInPoints());
                    }

                    if (cartProduct.getDiscount() == null) {
                        statement.setDouble(6, 0);
                    } else {
                        statement.setDouble(6, cartProduct.getDiscount().getPercentage());
                    }

                    statement.setInt(7, cartProduct.getStoreId());
                    statement.addBatch();

                    removeStockStatement.setInt(1, cartProduct.getStock());
                    removeStockStatement.setInt(2, cartProduct.getId());
                    removeStockStatement.addBatch();
                }
            }
            int[] rowsAffectedStock = removeStockStatement.executeBatch();
            removeStockStatement.close();

            // Check if all batches were successful
            for (int row : rowsAffectedStock) {
                if (row <= 0) {
                    return false;
                }
            }

            int[] rowsAffected = statement.executeBatch();
            statement.close();

            // Check if all batches were successful
            for (int row : rowsAffected) {
                if (row <= 0) {
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Product getProduct() {
        return product.get();
    }

    public ObjectProperty<Product> productProperty() {
        return product;
    }
}
