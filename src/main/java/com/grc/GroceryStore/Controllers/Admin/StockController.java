package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Category;
import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Models.Product;
import com.grc.GroceryStore.Utils.CustomField;
import com.grc.GroceryStore.Utils.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {
    public TextField name_fld;
    public TextField description_fld;
    public TextField price_fld;
    public TextField stock_fld;
    public TextField supplier_fld;
    public ChoiceBox<Category> category_chb;
    public Button add_btn;
    public Button update_btn;
    public Button clear_btn;
    public Button delete_btn;
    public TableView<Product> products_tbl;
    private final ObservableList<Product> products = Model.getInstance().getStore().getProducts();
    private final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(categories.isEmpty()){
            categories.addAll(Category.getCategoriesDB());
        }

        category_chb.getItems().addAll(categories);

        CustomField.intField(stock_fld);
        CustomField.doubleField(price_fld);

        // Set selection model to allow selecting only one row at a time
        products_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        products_tbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                name_fld.setText(newSelection.getName());
                description_fld.setText(newSelection.getDescription());
                price_fld.setText(Double.toString(newSelection.getPrice()));
                stock_fld.setText(Integer.toString(newSelection.getStock()));
                supplier_fld.setText(newSelection.getSupplierInfo());
                category_chb.setValue(newSelection.categoryProperty().get());
            }
        });

        this.products.clear();
        this.products.addAll(Product.getProductListDB());
        products_tbl.setItems(products);

        clear_btn.setOnAction(event -> onClear());
        add_btn.setOnAction(event -> onAdd());
        delete_btn.setOnAction(event -> onDelete());
        update_btn.setOnAction(event -> onUpdate());
    }

    public void onClear() {
        products_tbl.getSelectionModel().clearSelection();
        name_fld.clear();
        description_fld.clear();
        price_fld.clear();
        stock_fld.clear();
        category_chb.getSelectionModel().clearSelection();
        supplier_fld.clear();
    }
    public void onDelete() {
        Product selectedProduct = products_tbl.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Model.showError("Error", "Please select a product form the table to preform this action");
            return;
        }

        boolean isDeleted = Product.deleteProductByIdAsAdmin(selectedProduct.getId());
        if(!isDeleted){
            Model.showError("Error", "Could not delete the product.");
            return;
        }

        this.products.remove(selectedProduct);
        onClear();
    }
    public void onAdd() {
        String name = name_fld.getText().trim();
        String description = description_fld.getText().trim();
        String supplierInfo = supplier_fld.getText().trim();
        String priceString = price_fld.getText();
        String stockString = stock_fld.getText();
        Category category = category_chb.getValue();

        if(name.isEmpty() || description.isEmpty() || supplierInfo.isEmpty() || priceString.isEmpty() || stockString.isEmpty() || category == null){
            Model.showError("Error", "Empty fields");
            return;
        }

        double price = Double.parseDouble(priceString);
        int stock = Integer.parseInt(stockString);

        if(!Validation.isValidText(name)){
            Model.showError("Error", "Invalid name input, use only text.");
            return;
        }

        if(!Validation.isValidText(description)){
            Model.showError("Error", "Invalid description input, use only text.");
            return;
        }

        if (!Validation.isValidText(supplierInfo)){
            Model.showError("Error", "Invalid supplier input, use only text.");
            return;
        }

        Product newProduct = Product.createProductAsAdmin(category.getId(), name, description, price, stock, supplierInfo);
        if(newProduct == null){
            Model.showError("Error", "Could not create the product.");
            return;
        }

        products.add(newProduct);
        onClear();
    }

    public void onUpdate(){
        Product selectedProduct = products_tbl.getSelectionModel().getSelectedItem();
        if(selectedProduct == null){
            Model.showError("Error", "Please select a product form the table to preform this action");
            return;
        }

        String name = name_fld.getText().trim();
        String description = description_fld.getText().trim();
        String supplierInfo = supplier_fld.getText().trim();
        String priceString = price_fld.getText();
        String stockString = stock_fld.getText();
        Category category = category_chb.getValue();

        if(name.isEmpty() || description.isEmpty() || supplierInfo.isEmpty() || priceString.isEmpty() || stockString.isEmpty() || category == null){
            Model.showError("Error", "Empty fields");
            return;
        }

        double price = Double.parseDouble(priceString);
        int stock = Integer.parseInt(stockString);

        if(!Validation.isValidText(name)){
            Model.showError("Error", "Invalid name input, use only text.");
            return;
        }

        if(!Validation.isValidText(description)){
            Model.showError("Error", "Invalid description input, use only text.");
            return;
        }

        if (!Validation.isValidText(supplierInfo)){
            Model.showError("Error", "Invalid supplier input, use only text.");
            return;
        }

        Product updatedProduct = Product.updateProductAsAdmin(selectedProduct.getId(), category.getId(), name, description, price, stock, supplierInfo);
        if(updatedProduct == null){
            Model.showError("Error", "Could not update the product.");
            return;
        }

        products.set(products.indexOf(selectedProduct), updatedProduct);
        onClear();
    }
}
