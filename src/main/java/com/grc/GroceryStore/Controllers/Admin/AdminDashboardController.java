package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.*;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    public final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();
    public final ObservableList<Product> products = Model.getInstance().getStore().getProducts();
    private final ObservableList<SoldProduct> soldProducts = Model.getInstance().getStore().getSoldProducts();
    public Label welcome_lb;
    public Label total_sold_products_lb;
    public Label total_sales_lb;
    public TableView<SoldProduct> soldProduct_tb;
    public ArrayList<Integer> receiptIds = new ArrayList<>();
    public ChoiceBox<String> date_chb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] dates = {"Today", "This Month", "This Year"};
        date_chb.getItems().setAll(dates);
        date_chb.setValue(dates[0]);

        if (categories.isEmpty()) {
            categories.addAll(Category.getCategoriesDB());
        }

        if (products.isEmpty()) {
            products.addAll(Product.getProductListDB());
        }

        filterSoldProductsByDate(date_chb.getValue());

        date_chb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                filterSoldProductsByDate(newValue);
            }
        });

        soldProduct_tb.setItems(soldProducts);
        setWelcomeName();
    }

    private void filterSoldProductsByDate(String value){
        LocalDate date = null;

        switch (value) {
            case "Today" -> date = LocalDate.now();
            case "This Month" -> date = LocalDate.now().withDayOfMonth(1); // Start of the current month
            case "This Year" -> date = LocalDate.now().withDayOfYear(1); // Start of the current year
            default -> {
                Model.showError("Error", "Invalid choice");
                return;
            }
        }

        receiptIds = Receipt.getReceiptsIdsByDate(date);
        soldProducts.clear();
        for (int receiptId : receiptIds) {
            soldProducts.addAll(SoldProduct.getSoldProductsByReceiptIdFromDB(receiptId));
        }
        setTotalProfit();
        setTotalProductSales();
    }

    private void setWelcomeName() {
        String userName = Model.getInstance().getUser().getName();
        welcome_lb.setText("Welcome " + userName + " !");
    }

    private void setTotalProfit() {
        double total = 0;
        for (SoldProduct product : soldProducts) {
            total += product.getPaidMoney();
        }
        total_sales_lb.setText(formatMoneyString(total));
    }

    private void setTotalProductSales() {
        total_sold_products_lb.setText(Integer.toString(soldProducts.size()));
    }

    private String formatMoneyString(double amount){
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(amount) + "€";
    }
}


