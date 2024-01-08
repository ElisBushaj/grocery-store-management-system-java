package com.grc.GroceryStore.Controllers;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Models.User;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    public TextField name_fld;
    public TextField last_name_fld;
    public TextField email_fld;
    public Button update_btn;
    public Label role_lb;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setProfileData();
    }

    private void setProfileData(){
        User user = Model.getInstance().getUser();
        name_fld.setText(user.getName());
        last_name_fld.setText(user.getLastname());
        email_fld.setText(user.getEmail());
        role_lb.setText("Role: " + user.getRole());
    }

}
