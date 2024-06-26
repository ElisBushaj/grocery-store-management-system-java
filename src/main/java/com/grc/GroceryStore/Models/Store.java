package com.grc.GroceryStore.Models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Store {
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty pointsPerEuro;
    private final ObservableList<User> users;
    private final ObservableList<Category> categories;
    private final ObservableList<Product> products;
    private final ObservableList<ProductPoints> productPoints;
    private final ObservableList<Discount> discounts;
    private final ObservableList<SoldProduct> soldProducts;
    private final ObservableList<Receipt> receipts;
    private final ObservableList<Customer> customers;

    public Store(int id, String name, double pointsPerEuro) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.name = new SimpleStringProperty(this, "Name", name);
        this.pointsPerEuro = new SimpleDoubleProperty(this, "Points Per Euro", pointsPerEuro);
        this.users = FXCollections.observableArrayList();
        this.categories = FXCollections.observableArrayList();
        this.products = FXCollections.observableArrayList();
        this.productPoints = FXCollections.observableArrayList();
        this.discounts = FXCollections.observableArrayList();
        this.soldProducts = FXCollections.observableArrayList();
        this.receipts = FXCollections.observableArrayList();
        this.customers = FXCollections.observableArrayList();
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

    public double getPointsPerEuro() {
        return pointsPerEuro.get();
    }

    public DoubleProperty pointsPerEuroProperty() {
        return pointsPerEuro;
    }

    public ObservableList<User> getUsers() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }
        if(!Model.getInstance().getUser().isAdmin(true)){
            return null;
        }

        return users;
    }

    public ObservableList<Category> getCategories() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }

        return categories;
    }

    public Category findCategoryById(int targetId) {
        for (Category category : categories) {
            if (category.getId() == targetId) {
                return category;
            }
        }
        return null;
    }

    public ObservableList<Product> getProducts() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }

        return products;
    }

    public ObservableList<ProductPoints> getProductPoints() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }

        return productPoints;
    }

    public ProductPoints findProductPointsByProductId(int productId) {
        for (ProductPoints productPoints : productPoints) {
            if (productPoints.getProduct().getId() == productId) {
                return productPoints;
            }
        }
        return null;
    }

    public Product findProductById(int targetId) {
        for (Product product : products) {
            if (product.getId() == targetId) {
                return product;
            }
        }
        return null;
    }

    public ObservableList<Discount> getDiscounts() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }

        return discounts;
    }

    public Discount findDiscountByProductId(int productId) {
        for (Discount discount : discounts) {
            if (discount.getProduct().getId() == productId) {
                return discount;
            }
        }
        return null;
    }

    public ObservableList<SoldProduct> getSoldProducts() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }

        return soldProducts;
    }

    public ObservableList<Receipt> getReceipts() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }

        return receipts;
    }

    public ObservableList<Customer> getCustomers() {
        if(!Model.getInstance().isLoggedIn(true)){
            return null;
        }

        return customers;
    }

    public boolean updateStoreDB(String name, Double pointsPerEuro) {
        if(!Model.getInstance().isLoggedIn(true)) return false;

        String updateQuery = "UPDATE Store SET name = ?, pointsPerEuro = ? WHERE id = ?";
        try {
            PreparedStatement updateStatement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(updateQuery);
            updateStatement.setString(1, name);
            updateStatement.setDouble(2, pointsPerEuro);
            updateStatement.setInt(3, this.getId());

            int rowsAffected = updateStatement.executeUpdate();

            updateStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void clearLists(){
        this.users.clear();
        this.categories.clear();
        this.products.clear();
        this.productPoints.clear();
        this.discounts.clear();
        this.soldProducts.clear();
        this.receipts.clear();
        this.customers.clear();
    }
}
