package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.Customer;
import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Utils.Validation;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    private final ObservableList<Customer> customers = Model.getInstance().getStore().getCustomers();
    public TextField name_fld;
    public TextField last_name_fld;
    public TextField phone_fld;
    public ChoiceBox<String> gender_chb;
    public Button add_btn;
    public Button update_btn;
    public Button clear_btn;
    public Button delete_btn;
    public TableView<Customer> customer_tbl;
    public TextField points_fld;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] gender = {"male", "female", "other"};
        gender_chb.getItems().addAll(gender);

        // Set selection model to allow selecting only one row at a time
        customer_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        customer_tbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                name_fld.setText(newSelection.getName());
                last_name_fld.setText(newSelection.getLastname());
                phone_fld.setText(newSelection.getPhoneNumber());
                gender_chb.setValue(newSelection.getGender());
                points_fld.setText(String.valueOf(newSelection.getPoints()));
            }
        });

        this.customers.clear();
        this.customers.addAll(Customer.getCustomerList());
        customer_tbl.setItems(customers);

        clear_btn.setOnAction(event -> onClear());
        add_btn.setOnAction(event -> onAdd());
        delete_btn.setOnAction(event -> onDelete());
        update_btn.setOnAction(event -> onUpdate());
    }

    public void onClear() {
        customer_tbl.getSelectionModel().clearSelection();
        name_fld.clear();
        last_name_fld.clear();
        phone_fld.clear();
        gender_chb.getSelectionModel().clearSelection();
        points_fld.clear();
    }

    public void onDelete() {
        Customer selectedCustomer = customer_tbl.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Model.showError("Error", "Please select a customer from the table to preform this action");
            return;
        }

        boolean isDeleted = Customer.deleteCustomerById(selectedCustomer.getId());
        if (!isDeleted) {
            Model.showError("Error", "Could not delete the customer.");
            return;
        }

        this.customers.remove(selectedCustomer);
        onClear();
    }

    public void onAdd() {
        String name = name_fld.getText().trim();
        String lastname = last_name_fld.getText().trim();
        String phone = phone_fld.getText().trim();
        String gender = gender_chb.getValue();
        String pointsText = points_fld.getText();
        if (pointsText.isEmpty() || gender == null || name.isEmpty() || lastname.isEmpty() || phone.isEmpty()) {
            Model.showError("Error", "Empty fields");
            return;
        }
        int points = Integer.parseInt(points_fld.getText().trim());
        if (!Validation.validateCustomerInput(name, lastname, phone, gender, points)) {
            Model.showError("Error", "Invalid Input");
            return;
        }

        Customer newCustomer = Customer.createCustomer(name, lastname, phone, gender, points);
        if (newCustomer == null) {
            Model.showError("Error", "Could not add the customer.");
            return;
        }

        this.customers.add(newCustomer);
        onClear();
    }

    public void onUpdate() {
        Customer selectedCustomer = customer_tbl.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Model.showError("Error", "Please select an customer form the table to preform this action");
            return;
        }

        String name = name_fld.getText().trim();
        String lastname = last_name_fld.getText().trim();
        String phoneNumber = phone_fld.getText().trim();
        String gender = gender_chb.getValue().trim();
        int points = Integer.parseInt(points_fld.getText().trim());

        if (!Validation.validateCustomerInput(name, lastname, phoneNumber, gender, points)) {
            Model.showError("Error", "Invalid Input");
            return;
        }

        Customer updatedCustomer = Customer.updateCustomerById(selectedCustomer.getId(), name, lastname, phoneNumber, gender, points);
        if (updatedCustomer == null) {
            Model.showError("Error", "Could not update the customer.");
            return;
        }

        selectedCustomer.nameProperty().set(updatedCustomer.getName());
        selectedCustomer.lastnameProperty().set(updatedCustomer.getLastname());
        selectedCustomer.phoneNumberProperty().set(updatedCustomer.getPhoneNumber());
        selectedCustomer.genderProperty().set(updatedCustomer.getGender());

        onClear();
    }
}
