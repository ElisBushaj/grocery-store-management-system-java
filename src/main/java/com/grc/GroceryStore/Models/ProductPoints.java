package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductPoints {
    private final IntegerProperty id;
    private final ObjectProperty<Product> product;
    private final IntegerProperty priceInPoints;
    private final IntegerProperty storeId;


    public ProductPoints(int id, Product product, int priceInPoints, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.product = new SimpleObjectProperty<>(this, "Product", product);
        this.priceInPoints = new SimpleIntegerProperty(this, "Price In Points", priceInPoints);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getPriceInPoints() {
        return priceInPoints.get();
    }

    public IntegerProperty priceInPointsProperty() {
        return priceInPoints;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }

    public Product getProduct() {
        return product.get();
    }

    public ObjectProperty<Product> productProperty() {
        return product;
    }

    public static ArrayList<ProductPoints> getProductPointsDB() {
        ArrayList<ProductPoints> productPointsList = new ArrayList<>();

        try {
            String query = "SELECT * FROM ProductPoints WHERE storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, Model.getInstance().getStore().getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("productId");
                int priceInPoints = resultSet.getInt("priceInPoints");
                int storeId = resultSet.getInt("storeId");

                Product product = Model.getInstance().getStore().findProductById(productId);
                if(product == null){
                    continue;
                }

                ProductPoints productPoints = new ProductPoints(id, product, priceInPoints, storeId);
                productPointsList.add(productPoints);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productPointsList;
    }

    public static boolean deleteProductPointsByIdDB(int id) {
        if(!Model.getInstance().getUser().isAdmin(true)){
            return false;
        }

        try {
            String query = "DELETE FROM ProductPoints WHERE id = ? AND storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, id);
            statement.setInt(2, Model.getInstance().getStore().getId());

            int rowsAffected = statement.executeUpdate();
            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateProductPointsDB(int id, int priceInPoints, int productId) {
        if (!Model.getInstance().getUser().isAdmin(true)) {
            return false;
        }

        try {
            String query = "UPDATE ProductPoints SET priceInPoints = ? WHERE id = ? AND productId = ? AND storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, priceInPoints);
            statement.setInt(2, id);
            statement.setInt(3, productId);
            statement.setInt(4, Model.getInstance().getStore().getId());

            int rowsAffected = statement.executeUpdate();
            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ProductPoints createProductPointsDB(int priceInPoints, int productId) {
        if (!Model.getInstance().getUser().isAdmin(true)) {
            return null;
        }

        try {
            String query = "INSERT INTO ProductPoints (priceInPoints, productId, storeId) VALUES (?, ?, ?)";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1, priceInPoints);
            statement.setInt(2, productId);
            statement.setInt(3, Model.getInstance().getStore().getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);

                    Product product = Model.getInstance().getStore().findProductById(productId);
                    if(product == null){
                        return null;
                    }

                    return new ProductPoints(generatedId, product, priceInPoints, Model.getInstance().getStore().getId());
                }
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
