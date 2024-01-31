package com.grc.GroceryStore.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Customer {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty lastname;
    private final StringProperty phoneNumber;
    private final StringProperty gender;
    private final IntegerProperty points;
    private final IntegerProperty storeId;


    public Customer(int id, String name, String lastname, String phoneNumber, String gender, int points, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.name = new SimpleStringProperty(this, "Name", name);
        this.lastname = new SimpleStringProperty(this, "Lastname", lastname);
        this.phoneNumber = new SimpleStringProperty(this, "Phone Number", phoneNumber);
        this.gender = new SimpleStringProperty(this, "Gender", gender);
        this.points = new SimpleIntegerProperty(this, "Points", points);
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

    public String getLastname() {
        return lastname.get();
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }
    public static ArrayList<Customer> getCustomerList() {
        ArrayList<Customer> customerList = new ArrayList<>();

        try {
            String query = "SELECT * FROM Customer WHERE storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setInt(1, Model.getInstance().getStore().getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("id");
                String customerName = resultSet.getString("name");
                String customerLastName = resultSet.getString("lastname");
                String customerPhoneNumber = resultSet.getString("phoneNumber");
                String customerGender = resultSet.getString("gender");
                int customerPoints = resultSet.getInt("points");
                int customerStoreId = resultSet.getInt("storeId");

                Customer customer = new Customer(customerId, customerName, customerLastName, customerPhoneNumber, customerGender, customerPoints, customerStoreId);
                customerList.add(customer);
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    public static boolean deleteCustomerById(int id) {
        try {

            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement("DELETE FROM Customer WHERE id = ? AND storeId = ? LIMIT 1");
            statement.setInt(1, id);
            statement.setInt(2, Model.getInstance().getStore().getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static Customer createCustomer(String name, String lastname, String phoneNumber, String gender, int points) {
        int storeId = Model.getInstance().getStore().getId();

        try {
            String query = "INSERT INTO Customer (name, lastname, phoneNumber, gender, points, storeId) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, lastname);
            statement.setString(3, phoneNumber);
            statement.setString(4, gender);
            statement.setDouble(5, points);
            statement.setInt(6, storeId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int customerId = generatedKeys.getInt(1);
                    return new Customer(customerId, name, lastname, phoneNumber, gender, points, storeId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Customer updateCustomerById(int id, String name, String lastname, String phoneNumber, String gender, int points) {
        int storeId = Model.getInstance().getStore().getId();

        try {
            if (customerExists(id)) {
                String query = "UPDATE Customer SET name = ?, lastname = ?, phoneNumber = ?, gender = ?, points = ? WHERE id = ?";
                PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);

                statement.setString(1, name);
                statement.setString(2, lastname);
                statement.setString(3, phoneNumber);
                statement.setString(4, gender);
                statement.setInt(5, points);
                statement.setInt(6, id);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    return new Customer(id, name, lastname, phoneNumber, gender, points, storeId); // Assuming points are not updated in this operation
                } else {
                    Model.showError("Not Found", "Customer with ID " + id + " not found or no changes were made.");
                }
            } else {
                Model.showError("Not Found", "Customer with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null if the update was not successful or an error occurred
        return null;
    }

    private static boolean customerExists(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM Customer WHERE id = ?";
        PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }

        return false;
    }

    @Override
    public String toString(){
        return this.getName() + " " + this.getLastname();
    }

}
