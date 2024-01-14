package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button dashboard_btn;
    public Button stock_btn;
    public Button category_btn;
    public Button discount_btn;
    public Button point_btn;
    public Button employee_btn;
    public Button store_btn;
    public Button profile_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    private void addListener(){
        dashboard_btn.setOnAction(event -> onDashboard());
        profile_btn.setOnAction(event -> onProfile());
        employee_btn.setOnAction(event -> onEmployee());
        category_btn.setOnAction(event -> onCategories());
        logout_btn.setOnAction(event -> onLogout());
        stock_btn.setOnAction(event -> onStock());
        discount_btn.setOnAction(event -> onDiscount());
        point_btn.setOnAction(event -> onPoint());
        stock_btn.setOnAction(event -> onStock());
        store_btn.setOnAction(event -> onStore());
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }

    private void onProfile(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.PROFILE);
    }

    private void onEmployee(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.EMPLOYEE);
    }

    private void onCategories(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CATEGORY);
    }

    private void onStock(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.STOCK);
    }

    private void onDiscount(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DISCOUNT);
    }

    private void onPoint(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.POINT);
    }

    private void onStore(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.STORE);
    }

    private void onLogout(){
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
        Model.getInstance().logout(stage);
    }
}
