package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Models.SoldProduct;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CashierDashboardController implements Initializable {
    public Label welcome_lb;
    public Label total_profit_lb;
    public Label total_sold_product_lb;
    private final ObservableList<SoldProduct> soldProducts = Model.getInstance().getStore().getSoldProducts();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soldProducts.addAll(SoldProduct.getSoldProductsFromDB());
        total_sold_product_lb.setText(Integer.toString(soldProducts.size()));
        setWelcomeName();
        setTotalProfit();
    }

    private void setWelcomeName(){
        String userName = Model.getInstance().getUser().getName();
        welcome_lb.setText("Welcome " + userName + " !");
    }

    private void setTotalProfit(){
        double total = 0;
        for (SoldProduct product: soldProducts){
            total += product.getPaidMoney();
        }
        total_profit_lb.setText(Double.toString(total));
    }
}
