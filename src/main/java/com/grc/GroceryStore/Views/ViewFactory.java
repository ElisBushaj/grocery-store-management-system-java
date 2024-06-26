package com.grc.GroceryStore.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewFactory {
//    cashier views
    private VBox cashierDashboardView;
    private VBox sellView;
    private VBox customerView;

//    admin views
    private VBox adminDashboardView;
    private VBox employeeView;
    private VBox stockView;
    private VBox discountView;
    private VBox pointView;
    private VBox categoryView;
    private VBox storeView;
    private VBox profileView;

    private final ObjectProperty<CashierMenuOptions> clientSelectedMenuItem;
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;


    public ViewFactory(){
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<CashierMenuOptions> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public VBox getCashierDashboardView() {
        try {
            cashierDashboardView = new FXMLLoader(getClass().getResource("/Fxml/Cashier/CashierDashboard.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cashierDashboardView;
    }

    public VBox getSellView() {
        try {
            sellView = new FXMLLoader(getClass().getResource("/Fxml/Cashier/SellView.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sellView;
    }

    public VBox getCustomerView() {
        try {
            customerView = new FXMLLoader(getClass().getResource("/Fxml/Cashier/CustomerView.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerView;
    }

    public VBox getProfileView() {
        if (profileView == null){
            try {
                profileView = new FXMLLoader(getClass().getResource("/Fxml/ProfileView.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return profileView;
    }

//admin View getters
    public VBox getAdminDashboardView() {
        if (adminDashboardView == null){
            try {
                adminDashboardView = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminDashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return adminDashboardView;
    }

    public VBox getEmployeeView() {
        if (employeeView == null){
            try {
                employeeView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Employee.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return employeeView;
    }

    public VBox getCategoryView(){
        if(categoryView == null){
            try{
                categoryView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Categories.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return categoryView;
    }

    public VBox getStockView(){
        try {
            stockView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Stock.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockView;
    }

    public VBox getPointView(){
        try {
            pointView = new FXMLLoader(getClass().getResource("/Fxml/Admin/ProductPoints.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pointView;
    }

    public VBox getDiscountView(){
        try {
            discountView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Discount.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return discountView;
    }

    public VBox getStoreView(){
        if (storeView == null){
            try {
                storeView = new FXMLLoader(getClass().getResource("/Fxml/Admin/Store.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return storeView;
    }

    public void showCashierWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Cashier/Cashier.fxml"));
        createStage(loader);
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        createStage(loader);
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        stage.setScene(scene);

        stage.setTitle("Grocery Store");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}
