package com.grc.GroceryStore.Models;

import com.grc.GroceryStore.Utils.PasswordHashing;
import com.grc.GroceryStore.Views.ViewFactory;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class Model {
    private static Model model = null;
    private ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private final Store store;
    private User user;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.store = this.databaseDriver.populateDB();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }

    public void logout(Stage stage){
        this.user = null;
        this.viewFactory.closeStage(stage);

        this.viewFactory = new ViewFactory();
        this.viewFactory.showLoginWindow();
    }
    public void evaluateUserCred(String email, String password) {
        ResultSet resultSet = User.getUserByEmailDB(email);
        try {
            if (resultSet != null && resultSet.next() && PasswordHashing.verifyPassword(password, resultSet.getString("password"))) {
                this.user = new User(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getInt("storeId")
                );

            } else {
                showAlert("Login Failed", "Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public Store getStore() {
        return store;
    }

    public static void showAlert(String title,String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
