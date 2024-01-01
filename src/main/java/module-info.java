module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.grc.GroceryStore to javafx.fxml;
    exports com.grc.GroceryStore;
    exports com.grc.GroceryStore.Controllers;
    exports com.grc.GroceryStore.Controllers.Admin;
    exports com.grc.GroceryStore.Controllers.Cashier;
    exports com.grc.GroceryStore.Models;
    exports com.grc.GroceryStore.Views;
}