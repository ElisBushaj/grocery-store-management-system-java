package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Views.CashierMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class CashierMenuController implements Initializable {
    public Button dashboard_btn;
    public Button sell_btn;
    public Button add_costumer_btn;
    public Button profile_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    private void addListener(){
        dashboard_btn.setOnAction((event -> onDashboard()));
        sell_btn.setOnAction((event -> onSell()));
        add_costumer_btn.setOnAction((event -> onCostumer()));
        profile_btn.setOnAction((event -> onProfile()));
    }

    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(CashierMenuOptions.DASHBOARD);
    }

    private void onSell(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(CashierMenuOptions.SELL);
    }

    private void onCostumer(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(CashierMenuOptions.CUSTOMER);
    }

    private void onProfile(){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(CashierMenuOptions.PROFILE);
    }
}
