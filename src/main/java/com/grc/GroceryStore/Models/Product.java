package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Product {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final DoubleProperty price;
    private final IntegerProperty stock;
    private final StringProperty supplierInfo;
    private final IntegerProperty storeId;
    private final ObjectProperty<Category> category;

    public Product(int id, Category category, String name, String description, double price, int stock, String supplierInfo, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.name = new SimpleStringProperty(this, "Name", name);
        this.description = new SimpleStringProperty(this, "Description", description);
        this.price = new SimpleDoubleProperty(this, "Price", price);
        this.stock = new SimpleIntegerProperty(this, "Stock", stock);
        this.supplierInfo = new SimpleStringProperty(this, "Supplier Info", supplierInfo);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
        this.category = new SimpleObjectProperty<>(this, "Category", category);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public Category getCategory() {
        return category.get();
    }

    public ObjectProperty<Category> categoryProperty() {
        return category;
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

    public static ArrayList<Product> getProductListDB() {
        ArrayList<Product> productList = new ArrayList<>();

        try {
            String query = "SELECT * FROM Product WHERE storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, Model.getInstance().getStore().getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int categoryId = resultSet.getInt("categoryId");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                String supplierInfo = resultSet.getString("supplierInfo");
                int storeId = resultSet.getInt("storeId");

                Category category = Model.getInstance().getStore().findCategoryById(categoryId);
                if(category == null){
                    continue;
                }

                Product product = new Product(id, category, name, description, price, stock, supplierInfo, storeId);
                productList.add(product);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public static Product createProductAsAdmin(int categoryId, String name, String description, double price, int stock, String supplierInfo) {
        if (!Model.getInstance().getUser().isAdmin(true)) {
            return null;
        }

        try {
            String insertQuery = "INSERT INTO Product (categoryId, name, description, price, stock, supplierInfo, storeId) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            insertStatement.setInt(1, categoryId);
            insertStatement.setString(2, name);
            insertStatement.setString(3, description);
            insertStatement.setDouble(4, price);
            insertStatement.setInt(5, stock);
            insertStatement.setString(6, supplierInfo);
            insertStatement.setInt(7, Model.getInstance().getStore().getId());

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedProductId = generatedKeys.getInt(1);

                    Category category = Model.getInstance().getStore().findCategoryById(categoryId);
                    if(category == null){
                        return null;
                    }

                    return new Product(generatedProductId, category, name, description, price, stock, supplierInfo, Model.getInstance().getStore().getId());
                }
            }
            insertStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Product updateProductAsAdmin(int id, int categoryId, String name, String description, double price, int stock, String supplierInfo) {
        if (!Model.getInstance().getUser().isAdmin(true)) {
            return null;
        }

        try {
            if (productExists(id)) {
                String query = "UPDATE Product SET categoryId = ?, name = ?, description = ?, price = ?, stock = ?, supplierInfo = ? WHERE id = ?";
                PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);

                statement.setInt(1, categoryId);
                statement.setString(2, name);
                statement.setString(3, description);
                statement.setDouble(4, price);
                statement.setInt(5, stock);
                statement.setString(6, supplierInfo);
                statement.setInt(7, id);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    Category category = Model.getInstance().getStore().findCategoryById(categoryId);
                    if(category == null){
                        return null;
                    }

                    // Return the updated Product object
                    return new Product(id, category, name, description, price, stock, supplierInfo, Model.getInstance().getStore().getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean deleteProductByIdAsAdmin(int productId) {
        if (!Model.getInstance().getUser().isAdmin(true)) {
            return false;
        }

        try {
            if (productExists(productId)) {
                String query = "DELETE FROM Product WHERE id = ? AND storeId = ? LIMIT 1";
                PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);

                statement.setInt(1, productId);
                statement.setInt(2, Model.getInstance().getStore().getId());

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static boolean productExists(int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Product WHERE id = ?";
        try (PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query)) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
