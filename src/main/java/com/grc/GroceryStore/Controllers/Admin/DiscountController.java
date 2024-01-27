package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.*;
import com.grc.GroceryStore.Utils.CustomField;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class DiscountController implements Initializable {

    public TableView<Discount> product_discount_tbl;
    private final ObservableList<Discount> productDiscounts = Model.getInstance().getStore().getDiscounts();
    public ChoiceBox<Product> product_chb;
    public Button delete_btn;
    public Button clear_btn;
    public Button update_btn;
    public Button add_btn;
    public TextField discount_percentage_fld;
    private final ObservableList<Product> products = Model.getInstance().getStore().getProducts();
    private final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (categories.isEmpty()){
            categories.addAll(Category.getCategoriesDB());
        }

        if (products.isEmpty()){
            products.addAll(Product.getProductListDB());
        }

        product_chb.getItems().addAll(products);

        CustomField.doubleField(discount_percentage_fld);

        product_discount_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        product_discount_tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                product_chb.setValue(newValue.getProduct());
                discount_percentage_fld.setText(Double.toString(newValue.getPercentage()));
            }
        });

        productDiscounts.clear();
        productDiscounts.addAll(Discount.getDiscountsDB());
        product_discount_tbl.setItems(productDiscounts);
        clear_btn.setOnAction(event -> onClear());
        add_btn.setOnAction(event -> onAdd());
        delete_btn.setOnAction (event -> onDelete());
        update_btn.setOnAction(event -> onUpdate());
    }

    public void onClear() {
        discount_percentage_fld.clear();
        product_chb.getSelectionModel().clearSelection();
        product_discount_tbl.getSelectionModel().clearSelection();
    }

    public void onAdd(){
        String discountPercentageText = discount_percentage_fld.getText().trim();
        Product product = product_chb.getValue();

        if(discountPercentageText.isEmpty() || product == null){
            Model.showError("Error", "Empty fields");
            return;
        }

        double discountPercentage = Double.parseDouble(discountPercentageText);

        if (discountPercentage >1 || discountPercentage <=0) {
            Model.showError("Error", "Discount must be from 0 to 1");
            return;
        }

        Discount discount = Discount.createDiscountAsAdmin(discountPercentage, product.getId());

        if (discount == null){
            Model.showError("Error", "Could not create discount .");
            return;
        }

        productDiscounts.add(discount);
        onClear();
    }

    public void onDelete(){
        Discount selectedDiscount = product_discount_tbl.getSelectionModel().getSelectedItem();
        if(selectedDiscount == null){
            Model.showError("Error", "You need to select one row to complete this action.");
            return;
        }

        boolean isDeleted = Discount.deleteDiscountByIdDB(selectedDiscount.getId());
        if(!isDeleted){
            Model.showError("Error", "Could not delete the product.");
            return;
        }

        productDiscounts.remove(selectedDiscount);
        onClear();
    }

    public void onUpdate(){
        Discount selectedDiscount = product_discount_tbl.getSelectionModel().getSelectedItem();
        if(selectedDiscount == null){
            Model.showError("Error", "You need to select one row to complete this action.");
            return;
        }


        String DiscountText = discount_percentage_fld.getText().trim();
        Product product = product_chb.getValue();

        if(DiscountText.isEmpty() || product == null){
            Model.showError("Error", "Empty fields");
            return;
        }

        double discountPerc = Double.parseDouble(DiscountText);

        if (discountPerc <= 0 || discountPerc >1 ) {
            Model.showError("Error", "Discount must be between 0 and 1");
            return;
        }

        boolean isUpdated = Discount.updateDiscountDB(selectedDiscount.getId(), discountPerc, product.getId());
        if (!isUpdated){
            Model.showError("Error", "Could not update the row");
            return;
        }

        selectedDiscount.productProperty().set(product);
        selectedDiscount.percentageProperty().set(discountPerc);
        onClear();
    }

}

