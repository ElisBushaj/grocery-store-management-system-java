package com.grc.GroceryStore.Controllers.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {

    public TextField search_fld;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        search_fld.setText("ojojo");
        System.out.println("hrjkhkj");
    }
}
