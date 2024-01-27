package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Discount {
    private final IntegerProperty id;
    private final ObjectProperty<Product> product;
    private final DoubleProperty percentage;
    private final IntegerProperty storeId;

    public Discount(int id, Product product, double percentage, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.product = new SimpleObjectProperty<>(this, "Product", product);
        this.percentage = new SimpleDoubleProperty(this, "Percentage", percentage);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public static ArrayList<Discount> getDiscountsDB() {
        ArrayList<Discount> discounts = new ArrayList<>();
        try {
            String query = "SELECT * FROM Discount WHERE storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, Model.getInstance().getStore().getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productId = resultSet.getInt("productId");
                double percentage = resultSet.getDouble("percentage");

                Product product = Model.getInstance().getStore().findProductById(productId);
                if (product == null) {
                    continue;
                }

                Discount discount = new Discount(id, product, percentage, Model.getInstance().getStore().getId());
                discounts.add(discount);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return discounts;
    }

    public static Discount createDiscountAsAdmin(double percentage, int productId) {
        try {
            String query = "INSERT INTO Discount (productId, percentage, storeId) VALUES (?, ?, ?)";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, productId);
            statement.setDouble(2, percentage);
            statement.setInt(3, Model.getInstance().getStore().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating discount failed, no rows affected.");
            }

            // Retrieve the generated discount ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int discountId = generatedKeys.getInt(1);
                Product product = Model.getInstance().getStore().findProductById(productId);
                return new Discount(discountId, product, percentage, Model.getInstance().getStore().getId());
            } else {
                throw new SQLException("Creating discount failed, no ID obtained.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public double getPercentage() {
        return percentage.get();
    }

    public DoubleProperty percentageProperty() {
        return percentage;
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


    public static boolean deleteDiscountByIdDB(int discountId) {

        try {
            String query = "DELETE FROM Discount WHERE id = ? AND storeId = ?";

            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, discountId);
            statement.setInt(2,Model.getInstance().getStore().getId());

            // Execute the delete statement
            int rowsAffected = statement.executeUpdate();

            // Check if the deletion was successful
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return false;

    }
     public static boolean updateDiscountDB (int id , double percentage,int productId){
         if (!Model.getInstance().getUser().isAdmin(true)) {
             return false;
         }

         try {
             String query = "UPDATE Discount SET percentage = ? WHERE id = ? AND productId = ? AND storeId = ?";
             PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
             statement.setDouble(1,percentage);
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
}

