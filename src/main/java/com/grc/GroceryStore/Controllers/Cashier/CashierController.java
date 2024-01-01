package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CashierController implements Initializable {
    public BorderPane cashier_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener(((observableValue, oldValue, newValue) -> {
            switch (newValue){
                case SELL -> setCenter(Model.getInstance().getViewFactory().getSellView());
                case CUSTOMER -> setCenter(Model.getInstance().getViewFactory().getCustomerView());
                case PROFILE -> setCenter(Model.getInstance().getViewFactory().getProfileView());
                default -> setCenter(Model.getInstance().getViewFactory().getCashierDashboardView());
            }
        }));
    }

    private void setCenter(VBox vBox){
        cashier_parent.setCenter(vBox);
    }
}
