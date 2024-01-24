package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Category;
import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Utils.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {
    public TableView<Category> category_tbl;
    public TextField name_fld;
    public Button add_btn;
    public Button update_btn;
    public Button clear_btn;
    public Button delete_btn;
    private final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set selection model to allow selecting only one row at a time
        category_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        category_tbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                name_fld.setText(newSelection.getName());
            }
        });

        this.categories.clear();
        this.categories.addAll(Category.getCategoriesListAsAdmin());
        category_tbl.setItems(categories);

        clear_btn.setOnAction(event -> onClear());
        add_btn.setOnAction(event -> onAdd());
        delete_btn.setOnAction(event -> onDelete());
        update_btn.setOnAction(event -> onUpdate());
    }

    public void onClear() {
        category_tbl.getSelectionModel().clearSelection();
        name_fld.clear();
    }
    public void onDelete() {
        Category selectedCategory = category_tbl.getSelectionModel().getSelectedItem();
        if(selectedCategory == null){
            Model.showAlert("Error", "Please select an category form the table to preform this action");
            return;
        }

        boolean isDeleted = Category.deleteCategoryByIdAsAdmin(selectedCategory.getId());
        if(!isDeleted){
            Model.showAlert("Error", "Could not delete the category.");
            return;
        }

        this.categories.remove(selectedCategory);
        onClear();
    }
    public void onAdd() {
        String name = name_fld.getText().trim();

        if(!Validation.isValidName(name)){
            Model.showAlert("Error", "Invalid Input");
            return;
        }

        Category newCategory = Category.createCategoryAsAdmin(name);
        if (newCategory == null){
            Model.showAlert("Error", "Could not add the category.");
            return;
        }

        this.categories.add(newCategory);
        onClear();
    }
    public void onUpdate(){
        Category selectedCategory = category_tbl.getSelectionModel().getSelectedItem();
        if(selectedCategory == null){
            Model.showAlert("Error", "Please select an category form the table to preform this action");
            return;
        }

        String name = name_fld.getText().trim();

        if(!Validation.isValidName(name)){
            Model.showAlert("Error", "Invalid Input");
            return;
        }

        Category updatedCategory = Category.updateCategoryByIdAsAdmin(selectedCategory.getId(), name);
        if(updatedCategory == null){
            Model.showAlert("Error", "Could not update the user.");
            return;
        }

        selectedCategory.nameProperty().set(updatedCategory.getName());

        onClear();
    }
}
