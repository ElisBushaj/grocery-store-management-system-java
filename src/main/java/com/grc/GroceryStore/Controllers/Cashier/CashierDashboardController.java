package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.*;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class CashierDashboardController implements Initializable {
    public Label welcome_lb;
    public Label total_profit_lb;
    public final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();
    public final ObservableList<Product> products = Model.getInstance().getStore().getProducts();



    private final ObservableList<SoldProduct> soldProducts = Model.getInstance().getStore().getSoldProducts();
    public TableView dashboard_tb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (categories.isEmpty()){
            categories.addAll(Category.getCategoriesDB());
        }

        if (products.isEmpty()){
            products.addAll(Product.getProductListDB());
        }



       if(soldProducts.isEmpty()){
           soldProducts.addAll(SoldProduct.getSoldProductsFromDB());

       }
        dashboard_tb.getSelectionModel().setSelectionMode(null);
        dashboard_tb.setItems(soldProducts);
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
