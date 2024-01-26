package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Models.User;
import com.grc.GroceryStore.Utils.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    public TextField name_fld;
    public TextField last_name_fld;
    public TextField email_fld;
    public ChoiceBox<String> role_chb;
    public Button add_btn;
    public Button update_btn;
    public Button clear_btn;
    public Button delete_btn;
    public TableView<User> employee_tbl;
    private final ObservableList<User> users = Model.getInstance().getStore().getUsers();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] roles = {"admin", "cashier"};
        role_chb.getItems().addAll(roles);

        // Set selection model to allow selecting only one row at a time
        employee_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        employee_tbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                name_fld.setText(newSelection.getName());
                last_name_fld.setText(newSelection.getLastname());
                email_fld.setText(newSelection.getEmail());
                role_chb.setValue(newSelection.getRole());
            }
        });

        this.users.clear();
        this.users.addAll(Model.getInstance().getUser().getUserListAsAdmin());
        employee_tbl.setItems(users);

        clear_btn.setOnAction(event -> onClear());
        add_btn.setOnAction(event -> onAdd());
        delete_btn.setOnAction(event -> onDelete());
        update_btn.setOnAction(event -> onUpdate());
    }

    public void onClear() {
        employee_tbl.getSelectionModel().clearSelection();
        name_fld.clear();
        last_name_fld.clear();
        email_fld.clear();
        role_chb.getSelectionModel().clearSelection();
    }
    public void onDelete() {
        User selectedUser = employee_tbl.getSelectionModel().getSelectedItem();
        if(selectedUser == null){
            Model.showError("Error", "Please select an user form the table to preform this action");
            return;
        }

        boolean isDeleted = Model.getInstance().getUser().deleteUserByIdAsAdmin(selectedUser.getId());
        if(!isDeleted){
            Model.showError("Error", "Could not delete the employee.");
            return;
        }

        this.users.remove(selectedUser);
        onClear();
    }
    public void onAdd() {
        String name = name_fld.getText().trim();
        String lastname = last_name_fld.getText().trim();
        String email = email_fld.getText().trim();
        String role = role_chb.getValue();

        if(!Validation.validateUserInput(name, lastname, email, role)){
            Model.showError("Error", "Invalid Input");
            return;
        }

        User newUser = Model.getInstance().getUser().createUserAsAdmin(name, lastname, email, role);
        if (newUser == null){
            Model.showError("Error", "Could not add the employee.");
            return;
        }

        this.users.add(newUser);
        onClear();
    }
    public void onUpdate(){
        User selectedUser = employee_tbl.getSelectionModel().getSelectedItem();
        if(selectedUser == null){
            Model.showError("Error", "Please select an user form the table to preform this action");
            return;
        }

        String name = name_fld.getText().trim();
        String lastname = last_name_fld.getText().trim();
        String email = email_fld.getText().trim();
        String role = role_chb.getValue().trim();

        if(!Validation.validateUserInput(name, lastname, email, role)){
            Model.showError("Error", "Invalid Input");
            return;
        }

        User updatedUser = Model.getInstance().getUser().updateUserByIdAsAdmin(selectedUser.getId(), name, lastname, email, role);
        if(updatedUser == null){
            Model.showError("Error", "Could not update the user.");
            return;
        }

        selectedUser.nameProperty().set(updatedUser.getName());
        selectedUser.lastnameProperty().set(updatedUser.getLastname());
        selectedUser.emailProperty().set(updatedUser.getEmail());
        selectedUser.roleProperty().set(updatedUser.getRole());

        onClear();
    }
}
