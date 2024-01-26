package com.grc.GroceryStore.Controllers;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Utils.Validation;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public Button login_btn;
    public PasswordField password_fld;
    public TextField email_fld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(event -> onLogin());
    }

    private void onLogin() {
        String email = email_fld.getText().trim();
        if(!Validation.isValidEmail(email)){
            Model.showError("Error", "Invalid Email.");
            return;
        }

        Model.getInstance().evaluateUserCred(email, password_fld.getText());

        if (Model.getInstance().getUser() != null) {
            String role = Model.getInstance().getUser().getRole();
            Stage stage = (Stage) login_btn.getScene().getWindow();

            if (role.equals("admin")) {
                Model.getInstance().getViewFactory().closeStage(stage);
                Model.getInstance().getViewFactory().showAdminWindow();
                return;
            }
            if (role.equals("cashier")) {
                Model.getInstance().getViewFactory().closeStage(stage);
                Model.getInstance().getViewFactory().showCashierWindow();
            }
        }

    }
}
