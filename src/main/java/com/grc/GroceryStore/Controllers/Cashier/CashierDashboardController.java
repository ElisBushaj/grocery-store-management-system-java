package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.*;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CashierDashboardController implements Initializable {
    public final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();
    public final ObservableList<Product> products = Model.getInstance().getStore().getProducts();
    private final ObservableList<SoldProduct> soldProducts = Model.getInstance().getStore().getSoldProducts();
    public Label welcome_lb;
    public Label total_profit_lb;
    public TableView<SoldProduct> dashboard_tb;
    public Label total_product_sales_lb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (categories.isEmpty()) {
            categories.addAll(Category.getCategoriesDB());
        }

        if (products.isEmpty()) {
            products.addAll(Product.getProductListDB());
        }

        ArrayList<Integer> receiptIds = Receipt.getReceiptsIdsByUserId(Model.getInstance().getUser().getId());

        soldProducts.clear();
        for (int receiptId : receiptIds) {
            soldProducts.addAll(SoldProduct.getSoldProductsByReceiptIdFromDB(receiptId));
        }

        dashboard_tb.getSelectionModel().setSelectionMode(null);
        dashboard_tb.setItems(soldProducts);

        setTotalProfit();
        setTotalProductSales();
        setWelcomeName();
    }

    private void setWelcomeName() {
        String userName = Model.getInstance().getUser().getName();
        welcome_lb.setText("Welcome " + userName);
    }

    private void setTotalProfit() {
        double total = 0;
        for (SoldProduct product : soldProducts) {
            total += product.getPaidMoney();
        }
        total_profit_lb.setText(formatMoneyString(total));
    }

    public void setTotalProductSales() {
        total_product_sales_lb.setText(Integer.toString(soldProducts.size()));
    }

    private String formatMoneyString(double amount){
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(amount) + "€";
    }
}
