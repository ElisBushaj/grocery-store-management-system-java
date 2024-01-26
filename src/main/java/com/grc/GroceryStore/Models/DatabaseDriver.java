package com.grc.GroceryStore.Models;
import java.sql.*;

import com.grc.GroceryStore.Utils.PasswordHashing;

public class DatabaseDriver {
    private Connection connection;

    public DatabaseDriver(){
        try {
            String url = "jdbc:mysql://mysql-3ea8bd6-bushajelis2-24c7.a.aivencloud.com:20317/grocery-store";
            String user = "avnadmin";
            String password = "AVNS_F-kimu0XN-eHkitxbjL";
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Store populateDB() {
        Store store = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet storeSet = statement.executeQuery("SELECT * FROM Store LIMIT 1");

            if (storeSet.next()) {
                // Store exists, retrieve the storeId
                store = new Store(storeSet.getInt("id"), storeSet.getString("name"), storeSet.getDouble("pointsPerEuro"));
                System.out.println("Existing storeId retrieved from the Store table: " + store.getId());
            } else {
                // No store found, insert a new row into the Store table
                statement.executeUpdate("INSERT INTO Store (name, pointsPerEuro) VALUES ('GRC-Store', 0)", Statement.RETURN_GENERATED_KEYS);
                System.out.println("Inserted a new row into Store table.");

                // Retrieve the generated storeId
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    store = new Store(generatedKeys.getInt("id"), generatedKeys.getString("name"), generatedKeys.getDouble("pointsPerEuro"));
                    System.out.println("Newly generated storeId: " + store.getId());
                } else {
                    throw new SQLException("Failed to retrieve the generated storeId");
                }
            }

            ResultSet adminSet = statement.executeQuery("SELECT COUNT(*) FROM User WHERE role = 'admin' AND storeId = " + store.getId());

            adminSet.next();
            int adminRowCount = adminSet.getInt(1);

            if (adminRowCount == 0) {
                // No admin user found for this store, insert a new admin user
                String hashedPassword = PasswordHashing.generateHashedPassword("admin");

                // Inserting an admin user into the User table
                String insertAdminQuery = "INSERT INTO User (name, lastname, email, password, role, storeId) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertAdminStatement = connection.prepareStatement(insertAdminQuery);
                insertAdminStatement.setString(1, "admin");
                insertAdminStatement.setString(2, "admin");
                insertAdminStatement.setString(3, "admin@admin.com");
                insertAdminStatement.setString(4, hashedPassword);
                insertAdminStatement.setString(5, "admin");
                insertAdminStatement.setInt(6, store.getId());

                insertAdminStatement.executeUpdate();
                insertAdminStatement.close();
                System.out.println("Admin user inserted into User table.");
            } else {
                System.out.println("Admin user already exists for this store.");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return store;
    }
}
