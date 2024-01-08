package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CashierDashboardController implements Initializable {
    public Label welcome_lb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setWelcomeName();
    }

    private void setWelcomeName(){
        String userName = Model.getInstance().getUser().getName();
        welcome_lb.setText("Welcome " + userName + " !");
    }
}
