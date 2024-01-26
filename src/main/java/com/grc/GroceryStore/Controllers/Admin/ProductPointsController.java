package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Category;
import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Models.Product;
import com.grc.GroceryStore.Models.ProductPoints;
import com.grc.GroceryStore.Utils.CustomField;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductPointsController implements Initializable {
    public TableView<ProductPoints> product_points_tbl;
    private final ObservableList<ProductPoints> productPoints = Model.getInstance().getStore().getProductPoints();
    public ChoiceBox<Product> product_chb;
    public TextField price_in_points_fld;
    public Button add_btn;
    public Button update_btn;
    public Button clear_btn;
    public Button delete_btn;

    private final ObservableList<Product> products = Model.getInstance().getStore().getProducts();
    private final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (categories.isEmpty()){
            categories.addAll(Category.getCategoriesListAsAdmin());
        }

        if (products.isEmpty()){
            products.addAll(Product.getProductListDB());
        }

        product_chb.getItems().addAll(products);

        CustomField.intField(price_in_points_fld);

        product_points_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        product_points_tbl.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                product_chb.setValue(newValue.getProduct());
                price_in_points_fld.setText(Integer.toString(newValue.getPriceInPoints()));
            }
        });

        productPoints.clear();
        productPoints.addAll(ProductPoints.getProductPointsDB());
        product_points_tbl.setItems(productPoints);

        clear_btn.setOnAction(event -> onClear());
        add_btn.setOnAction(event -> onAdd());
        delete_btn.setOnAction(event -> onDelete());
        update_btn.setOnAction(event -> onUpdate());
    }

    public void onClear() {
        price_in_points_fld.clear();
        product_chb.getSelectionModel().clearSelection();
        product_points_tbl.getSelectionModel().clearSelection();
    }

    public void onAdd(){
        String priceInPointsText = price_in_points_fld.getText().trim();
        Product product = product_chb.getValue();

        if(priceInPointsText.isEmpty() || product == null){
            Model.showError("Error", "Empty fields");
            return;
        }

        int priceInPoints = Integer.parseInt(priceInPointsText);

        if (priceInPoints <= 0) {
            Model.showError("Error", "Price in points must be a positive integer");
            return;
        }

        ProductPoints newProductPoints = ProductPoints.createProductPointsDB(priceInPoints, product.getId());

        if (newProductPoints == null){
            Model.showError("Error", "Could not create product points.");
            return;
        }

        productPoints.add(newProductPoints);
        onClear();
    }

    public void onDelete(){
        ProductPoints selectedProductPoints = product_points_tbl.getSelectionModel().getSelectedItem();
        if(selectedProductPoints == null){
            Model.showError("Error", "You need to select one row to complete this action.");
            return;
        }

        boolean isDeleted = ProductPoints.deleteProductPointsByIdDB(selectedProductPoints.getId());
        if(!isDeleted){
            Model.showError("Error", "Could not delete the product.");
            return;
        }

        productPoints.remove(selectedProductPoints);
        onClear();
    }

    public void onUpdate(){
        ProductPoints selectedProductPoints = product_points_tbl.getSelectionModel().getSelectedItem();
        if(selectedProductPoints == null){
            Model.showError("Error", "You need to select one row to complete this action.");
            return;
        }

        String priceInPointsText = price_in_points_fld.getText().trim();
        Product product = product_chb.getValue();

        if(priceInPointsText.isEmpty() || product == null){
            Model.showError("Error", "Empty fields");
            return;
        }

        int priceInPoints = Integer.parseInt(priceInPointsText);

        if (priceInPoints <= 0) {
            Model.showError("Error", "Price in points must be a positive integer");
            return;
        }

        boolean isUpdated = ProductPoints.updateProductPointsDB(selectedProductPoints.getId(), priceInPoints, product.getId());
        if (!isUpdated){
            Model.showError("Error", "Could not update the row");
            return;
        }

        selectedProductPoints.productProperty().set(product);
        selectedProductPoints.priceInPointsProperty().set(priceInPoints);
        onClear();
    }

}
