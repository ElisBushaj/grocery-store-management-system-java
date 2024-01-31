package com.grc.GroceryStore.Models;

import javafx.beans.property.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Receipt {
    private final IntegerProperty id;
    private final IntegerProperty customerId;
    private final IntegerProperty userId;
    private final ObjectProperty<Date> date;
    private final DoubleProperty price;
    private final IntegerProperty storeId;

    public Receipt(int id, int customerId, int userId, Date date, double price, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.customerId = new SimpleIntegerProperty(this, "Customer Id", customerId);
        this.userId = new SimpleIntegerProperty(this, "User Id", userId);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.price = new SimpleDoubleProperty(this, "Price", price);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public Receipt(int id, int userId, Date date, double price, int storeId) {
        this.id = new SimpleIntegerProperty(this, "Id", id);
        this.customerId = new SimpleIntegerProperty();
        this.userId = new SimpleIntegerProperty(this, "User Id", userId);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.price = new SimpleDoubleProperty(this, "Price", price);
        this.storeId = new SimpleIntegerProperty(this, "Store Id", storeId);
    }

    public static ArrayList<Integer> getReceiptsIdsByUserId(int userId) {
        ArrayList<Integer> receipts = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        String query = "SELECT id FROM Receipt WHERE userId = ? AND storeId = ? AND date=?";
        try {
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);

            statement.setInt(1, userId);
            statement.setInt(2, Model.getInstance().getStore().getId());
            statement.setString(3,formattedDate);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int receiptId = rs.getInt("id");
                receipts.add(receiptId);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receipts;
    }


    public static ArrayList<Integer> getReceiptsIdsByDate(LocalDate date) {
        ArrayList<Integer> receipts = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);

        String query = "SELECT id FROM Receipt  WHERE storeId = ? AND date >= ?";
        try {
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query);


            statement.setInt(1, Model.getInstance().getStore().getId());
            statement.setString(2,formattedDate);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int receiptId = rs.getInt("id");
                receipts.add(receiptId);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return receipts;
    }

    public static Receipt createReceiptDB(int customerId, int userId, double price) {
        try {
            String query = "INSERT INTO Receipt (customerId, userId, date, price, storeId) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1, customerId);
            statement.setInt(2, userId);
            statement.setDouble(3, price);
            statement.setInt(4, Model.getInstance().getStore().getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new Receipt(generatedId, customerId, userId, new Date(), price, Model.getInstance().getStore().getId());
                }
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Receipt createReceiptDB(int userId, double price) {
        try {
            String query = "INSERT INTO Receipt (userId, date, price, storeId) VALUES (?, CURDATE(), ?, ?)";
            PreparedStatement statement = Model.getInstance().getDatabaseDriver().getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1, userId);
            statement.setDouble(2, price);
            statement.setInt(3, Model.getInstance().getStore().getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new Receipt(generatedId, userId, new Date(), price, Model.getInstance().getStore().getId());
                }
            }

            statement.close();
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

    public int getCustomerId() {
        return customerId.get();
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public int getStoreId() {
        return storeId.get();
    }

    public IntegerProperty storeIdProperty() {
        return storeId;
    }
}

