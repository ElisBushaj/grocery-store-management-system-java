package com.grc.GroceryStore.Models;

import com.grc.GroceryStore.Utils.PasswordHashing;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty lastname;
    private final StringProperty email;
    private final StringProperty role;
    private final IntegerProperty storeId;

    public User(int id, String name, String lastname, String email, String role, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.name = new SimpleStringProperty(this, "Name", name);
        this.lastname = new SimpleStringProperty(this, "Last Name", lastname);
        this.email = new SimpleStringProperty(this, "Email", email);
        this.role = new SimpleStringProperty(this, "Role", role);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public static ResultSet getUserByEmailDB(String email) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM User WHERE email = ? AND storeId = ? LIMIT 1";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setString(1, email);
            statement.setInt(2, Model.getInstance().getStore().getId());
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
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

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }

    public User createUserAsAdmin(String name, String lastname, String email, String role) {
        if (!this.role.get().equals("admin")) {
            Model.showAlert("Unauthorised", "This action can be done by an Admin.");
            return null;
        }

        int storeId = Model.getInstance().getStore().getId();
        String defaultPassword = PasswordHashing.generateHashedPassword("cashier123");
        try {
            Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
            String query = "INSERT INTO User (name, lastname, email, password, role, storeId) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, lastname);
            statement.setString(3, email);
            statement.setString(4, defaultPassword);
            statement.setString(5, role);
            statement.setInt(6, storeId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    // Create and return a User object using the retrieved user ID and other details
                    return new User(userId, name, lastname, email, role, storeId);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User creation failed
    }

    public User updateUserByIdAsAdmin(int id, String name, String lastname, String email, String role) {
        if (!this.role.get().equals("admin")) {
            Model.showAlert("Unauthorised", "This action can be done by an Admin.");
            return null;
        }

        int storeId = Model.getInstance().getStore().getId();
        try {
            if (userExists(id)) {
                String query = "UPDATE User SET name = ?, lastname = ?, email = ?, role = ? WHERE id = ?";
                PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);

                statement.setString(1, name);
                statement.setString(2, lastname);
                statement.setString(3, email);
                statement.setString(4, role);
                statement.setInt(5, id);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    return new User(id, name, lastname, email, role, storeId);
                } else {
                    Model.showAlert("Not Found", "User with ID " + id + " not found or no changes were made.");
                }
            } else {
                Model.showAlert("Not Found", "User with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null if the update was not successful or an error occurred
        return null;
    }

    // Method to check if a user with the specified ID exists
    private boolean userExists(int userId) throws SQLException {
        String query = "SELECT COUNT(*) FROM User WHERE id = ?";
        PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
        statement.setInt(1, userId);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);

        return count > 0;
    }

    public boolean deleteUserByIdAsAdmin(int id) {
        if (!this.role.get().equals("admin")) {
            Model.showAlert("Unauthorised", "This action can be done by an Admin.");
            return false;
        }

        try {
            Connection connection = Model.getInstance().getDatabaseDriver().getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM User WHERE id = ? AND storeId = ? LIMIT 1");
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

    public ArrayList<User> getUserListAsAdmin() {
        if (!this.role.get().equals("admin")) {
            Model.showAlert("Unauthorised", "This action can be done by an Admin.");
            return null;
        }

        try {
            String query = "SELECT * FROM User WHERE email != ? AND storeId = ?";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);
            statement.setString(1, email.get());
            statement.setInt(2, Model.getInstance().getStore().getId());

            ResultSet resultSet = statement.executeQuery();

            ArrayList<User> userList = new ArrayList<>();
            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String userName = resultSet.getString("name");
                String userLastName = resultSet.getString("lastname");
                String userEmail = resultSet.getString("email");
                String userRole = resultSet.getString("role");
                int userStoreId = resultSet.getInt("storeId");

                User user = new User(userId, userName, userLastName, userEmail, userRole, userStoreId);
                userList.add(user);
            }

            statement.close();
            resultSet.close();
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isAdmin(boolean showAlert){
        boolean isAdmin = this.role.get().equals("admin");
        if (showAlert && !isAdmin){
            Model.showAlert("Unauthorised", "You need to be an admin to complete this action.");
        }
        return isAdmin;
    }

    public boolean isCashier(boolean showAlert){
        boolean isCashier = this.role.get().equals("cashier");
        if(showAlert && !isCashier){
            Model.showAlert("Unauthorised", "You need to be an cashier to complete this action");
        }
        return isCashier;
    }
}
