package com.grc.GroceryStore.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Category {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty storeId;

    public Category(int id, String name, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.name = new SimpleStringProperty(this, "Name", name);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }

    public static ArrayList<Category> getCategoriesDB() {
        ArrayList<Category> categoriesList = new ArrayList<>();

        try {
            String query = "SELECT * FROM Category WHERE storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, Model.getInstance().getStore().getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("id");
                String categoryName = resultSet.getString("name");
                int categoryStoreId = resultSet.getInt("storeId");

                Category category = new Category(categoryId, categoryName, categoryStoreId);
                categoriesList.add(category);
            }

            statement.close();
            resultSet.close();
            return categoriesList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoriesList;
    }

    public static boolean deleteCategoryByIdAsAdmin(int id) {
        if (!Model.getInstance().getUser().getRole().equals("admin")) {
            Model.showError("Unauthorised", "This action can be done by an Admin.");
            return false;
        }

        try {
            String query = "DELETE FROM Category WHERE id = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            statement.close();

            if (rowsAffected > 0) {
                // Category successfully deleted
                return true;
            } else {
                // Handle the case where no rows were affected (category with the specified ID not found)
                Model.showError("Error", "Category with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions or return false based on the use case
        }

        return false;
    }

    public static Category createCategoryAsAdmin(String categoryName) {
        if (!Model.getInstance().getUser().getRole().equals("admin")) {
            Model.showError("Unauthorised", "This action can be done by an Admin.");
            return null;
        }

        try {
            String insertQuery = "INSERT INTO Category (name, storeId) VALUES (?, ?)";
            PreparedStatement insertStatement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            insertStatement.setString(1, categoryName);
            insertStatement.setInt(2, Model.getInstance().getStore().getId());

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Category successfully created, retrieve the generated ID
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedCategoryId = generatedKeys.getInt(1);

                    // Return the newly created Category object
                    return new Category(generatedCategoryId, categoryName, Model.getInstance().getStore().getId());
                }
            } else {
                Model.showError("Error", "Failed to create the category.");
            }

            insertStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions or return null based on the use case
        }

        return null;
    }
    public static Category updateCategoryByIdAsAdmin(int categoryId, String newName) {
        if (!Model.getInstance().getUser().getRole().equals("admin")) {
            Model.showError("Unauthorised", "This action can be done by an Admin.");
            return null;
        }

        try {
            String updateQuery = "UPDATE Category SET name = ? WHERE id = ?";
            PreparedStatement updateStatement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(updateQuery);

            updateStatement.setString(1, newName);
            updateStatement.setInt(2, categoryId);

            int rowsAffected = updateStatement.executeUpdate();

            updateStatement.close();

            if (rowsAffected > 0) {
                // Category successfully updated, retrieve the updated category
                return getCategoryById(categoryId);
            } else {
                Model.showError("Error", "Category with ID " + categoryId + " not found or no changes were made.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions or return null based on the use case
        }

        return null;
    }
    private static Category getCategoryById(int categoryId) throws SQLException {
        String selectQuery = "SELECT * FROM Category WHERE id = ?";
        PreparedStatement selectStatement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(selectQuery);
        selectStatement.setInt(1, categoryId);

        ResultSet resultSet = selectStatement.executeQuery();

        if (resultSet.next()) {
            // Create and return a Category object from the retrieved data
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int storeId = resultSet.getInt("storeId");

            return new Category(id, name, storeId);
        }

        // Return null if the category with the specified ID is not found
        return null;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
