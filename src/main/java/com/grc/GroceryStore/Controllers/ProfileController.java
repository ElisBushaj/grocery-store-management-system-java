package com.grc.GroceryStore.Controllers;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Models.User;
import com.grc.GroceryStore.Utils.Validation;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    public Button update_btn;
    public Label role_lb;
    public Label name_lb;
    public Label last_name_lb;
    public Label email_lb;
    public PasswordField old_password_fld;
    public PasswordField new_password_fld;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setProfileData();
        update_btn.setOnAction((event -> onUpdate()));
    }

    private void setProfileData(){
        User user = Model.getInstance().getUser();
        name_lb.setText(user.getName());
        last_name_lb.setText(user.getLastname());
        email_lb.setText(user.getEmail());
        role_lb.setText(user.getRole());
    }

    private void onUpdate(){
        String newPassword = new_password_fld.getText();
        String oldPassword = old_password_fld.getText();
        if(newPassword.isEmpty() || oldPassword.isEmpty()){
            Model.showError("Error", "Empty fields.");
            return;
        }

        boolean isValidPassword = Validation.isValidPassword(newPassword);
        if(!isValidPassword){
            Model.showError("Error", "New password should be at least 8 characters long.");
            return;
        }

        boolean isSamePassword = Model.getInstance().getUser().isSamePassword(oldPassword);
        if(!isSamePassword){
            Model.showError("Error", "Old password does not match.");
            return;
        }

        boolean isPasswordChanged = Model.getInstance().getUser().changePasswordDB(newPassword);
        if(!isPasswordChanged){
            Model.showError("Error", "Could not change the password.");
            return;
        }

        clear();
        Model.showAlert("Success", "Password was changed successfully.", Alert.AlertType.INFORMATION);

    }

    private void clear(){
        old_password_fld.clear();
        new_password_fld.clear();
    }
}
