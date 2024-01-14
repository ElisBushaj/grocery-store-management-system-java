package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Category;
import com.grc.GroceryStore.Models.Product;
import javafx.collections.FXCollections;
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
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        category_chb.getItems().addAll(Category.getCategoriesListAsAdmin());

        // Set selection model to allow selecting only one row at a time
        products_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        products_tbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                name_fld.setText(newSelection.getName());
                description_fld.setText(newSelection.getDescription());
            }
        });

        this.products.clear();
//        this.products.addAll(Product.getUserListAsAdmin());
        products_tbl.setItems(products);

        clear_btn.setOnAction(event -> onClear());
//        add_btn.setOnAction(event -> onAdd());
//        delete_btn.setOnAction(event -> onDelete());
//        update_btn.setOnAction(event -> onUpdate());
    }

    public void onClear() {
        products_tbl.getSelectionModel().clearSelection();
        name_fld.clear();
        description_fld.clear();
        price_fld.clear();
        category_chb.getSelectionModel().clearSelection();
    }
//    public void onDelete() {
//        User selectedUser = products_tbl.getSelectionModel().getSelectedItem();
//        if(selectedUser == null){
//            Model.showAlert("Error", "Please select a user form the table to preform this action");
//            return;
//        }
//
//        boolean isDeleted = Model.getInstance().getUser().deleteUserByIdAsAdmin(selectedUser.getId());
//        if(!isDeleted){
//            Model.showAlert("Error", "Could not delete the employee.");
//            return;
//        }
//
//        this.products.remove(selectedUser);
//        onClear();
//    }
//    public void onAdd() {
//        String name = name_fld.getText().trim();
//        String lastname = description_fld.getText().trim();
//        String email = price_fld.getText().trim();
//        String role = category_chb.getValue().trim();
//
//        if(!Validation.validateUserInput(name, lastname, email, role)){
//            Model.showAlert("Error", "Invalid Input");
//            return;
//        }
//
//        User newUser = Model.getInstance().getUser().createUserAsAdmin(name, lastname, email, role);
//        if (newUser == null){
//            Model.showAlert("Error", "Could not add the employee.");
//            return;
//        }
//
//        this.products.add(newUser);
//        onClear();
//    }
//    public void onUpdate(){
//        User selectedUser = products_tbl.getSelectionModel().getSelectedItem();
//        if(selectedUser == null){
//            Model.showAlert("Error", "Please select a user form the table to preform this action");
//            return;
//        }
//
//        String name = name_fld.getText().trim();
//        String lastname = description_fld.getText().trim();
//        String email = price_fld.getText().trim();
//        String role = category_chb.getValue().trim();
//
//        if(!Validation.validateUserInput(name, lastname, email, role)){
//            Model.showAlert("Error", "Invalid Input");
//            return;
//        }
//
//        User updatedUser = Model.getInstance().getUser().updateUserByIdAsAdmin(selectedUser.getId(), name, lastname, email, role);
//        if(updatedUser == null){
//            Model.showAlert("Error", "Could not update the user.");
//            return;
//        }
//
//        selectedUser.nameProperty().set(updatedUser.getName());
//        selectedUser.lastnameProperty().set(updatedUser.getLastname());
//        selectedUser.emailProperty().set(updatedUser.getEmail());
//        selectedUser.roleProperty().set(updatedUser.getRole());
//
//        onClear();
//    }
}
