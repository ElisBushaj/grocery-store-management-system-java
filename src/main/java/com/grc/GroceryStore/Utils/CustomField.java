package com.grc.GroceryStore.Utils;

import javafx.scene.control.TextField;

public class CustomField {
    private static final String doubleRegex = "[^\\d.]";
    private static final String intRegex = "[^\\d]";

    public static void doubleField(TextField field){
        addListener(field, doubleRegex);
    }

    public static void intField(TextField field){
        addListener(field, intRegex);
    }

    private static void addListener(TextField field, String regex){
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            String numericValue = newValue.replaceAll(regex, "");
            field.setText(numericValue);
        });
    }
}
