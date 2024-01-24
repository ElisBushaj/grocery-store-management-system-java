module com.grc.GroceryStore {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;


    opens com.grc.GroceryStore to javafx.fxml;
    exports com.grc.GroceryStore;
    exports com.grc.GroceryStore.Controllers;
    exports com.grc.GroceryStore.Controllers.Admin;
    exports com.grc.GroceryStore.Controllers.Cashier;
    exports com.grc.GroceryStore.Models;
    exports com.grc.GroceryStore.Views;
    exports com.grc.GroceryStore.Utils;
}