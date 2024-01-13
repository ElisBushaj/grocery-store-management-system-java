package com.grc.GroceryStore.Views;

import com.grc.GroceryStore.Controllers.Admin.AdminController;
import com.grc.GroceryStore.Controllers.Cashier.CashierController;
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
        if (cashierDashboardView == null){
            try {
                cashierDashboardView = new FXMLLoader(getClass().getResource("/Fxml/Cashier/CashierDashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return cashierDashboardView;
    }

    public VBox getSellView() {
        if (sellView == null){
            try {
                sellView = new FXMLLoader(getClass().getResource("/Fxml/Cashier/SellView.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sellView;
    }

    public VBox getCustomerView() {
        if (customerView == null){
            try {
                customerView = new FXMLLoader(getClass().getResource("/Fxml/Cashier/CustomerView.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    public void showCashierWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Cashier/Cashier.fxml"));
        CashierController cashierController = new CashierController();
        loader.setController(cashierController);
        createStage(loader);
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
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
        stage.setScene(scene);

        stage.setTitle("Grocery Store");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}
