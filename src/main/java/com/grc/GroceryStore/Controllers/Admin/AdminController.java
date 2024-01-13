package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener(((observableValue, oldValue, newValue) -> {
            switch (newValue){
                case PROFILE -> setCenter(Model.getInstance().getViewFactory().getProfileView());
                case EMPLOYEE -> setCenter(Model.getInstance().getViewFactory().getEmployeeView());
                case CATEGORY -> setCenter(Model.getInstance().getViewFactory().getCategoryView());
                default -> setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
            }
        }));
    }


    private void setCenter(VBox vBox){
        admin_parent.setCenter(vBox);
    }

}
