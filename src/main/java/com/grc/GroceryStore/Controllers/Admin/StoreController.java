package com.grc.GroceryStore.Controllers.Admin;

import com.grc.GroceryStore.Models.Model;
import com.grc.GroceryStore.Utils.CustomField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class StoreController implements Initializable {
    public TextField name_fld;
    public TextField points_per_euro_fld;
    public Button update_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CustomField.doubleField(points_per_euro_fld);
        setFields();
        update_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onUpdate();
            }
        });
    }

    private void setFields(){
        name_fld.setText(Model.getInstance().getStore().getName());
        points_per_euro_fld.setText(Double.toString(Model.getInstance().getStore().getPointsPerEuro()));
    }

    private void onUpdate(){
        String name = name_fld.getText().trim();
        String pointsPerEuroString = points_per_euro_fld.getText();
        if(name.isEmpty() || pointsPerEuroString.isEmpty()){
            Model.showError("Error", "Empty fields.");
            return;
        }

        double pointsPerEuro = Double.parseDouble(pointsPerEuroString);
        if(pointsPerEuro < 0){
            Model.showError("Error", "Points/Euro should be positive or 0.");
            return;
        }

        boolean isStoreUpdated = Model.getInstance().getStore().updateStoreDB(name, pointsPerEuro);
        if(!isStoreUpdated){
            Model.showError("Error", "Could not update the store.");
            return;
        }

        Model.getInstance().getStore().nameProperty().set(name);
        Model.getInstance().getStore().pointsPerEuroProperty().set(pointsPerEuro);
        Model.showAlert("Success", "Store updated successfully", Alert.AlertType.INFORMATION);
    }
}
