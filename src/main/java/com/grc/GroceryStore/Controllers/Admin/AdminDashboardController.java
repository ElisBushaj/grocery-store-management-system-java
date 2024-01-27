package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.*;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    public Label welcome_lb;
    public Label total_soldP_lb;
    public Label total_sales_lb;
    public final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();
    public final ObservableList<Product> products = Model.getInstance().getStore().getProducts();
    private final ObservableList<SoldProduct> soldProducts = Model.getInstance().getStore().getSoldProducts();
    public TableView<SoldProduct> soldProduct_tb;
    public ArrayList<Integer> receiptIds = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (categories.isEmpty()) {
            categories.addAll(Category.getCategoriesDB());
        }

        if (products.isEmpty()) {
            products.addAll(Product.getProductListDB());
        }




            if (soldProducts.isEmpty()) {
                receiptIds = Receipt.getReceiptsIdsByDate();

                for (int receiptId : receiptIds) {
                    soldProducts.addAll(SoldProduct.getSoldProductsByReceiptIdFromDB(receiptId));
                }

        }

        soldProduct_tb.getSelectionModel().setSelectionMode(null);
        soldProduct_tb.setItems(soldProducts);
        setTotalProfit();
        setTotalProductSales();
        setWelcomeName();
    }



    private void setWelcomeName(){
        String userName = Model.getInstance().getUser().getName();
        welcome_lb.setText("Welcome " + userName + " !");
    }

    private void setTotalProfit() {
        double total = 0;
        for (SoldProduct product : soldProducts) {
            total += product.getPaidMoney();
        }
        total_sales_lb.setText(Double.toString(total));
    }

    public void setTotalProductSales() {
        total_soldP_lb.setText(Integer.toString(soldProducts.size()));
    }
}


