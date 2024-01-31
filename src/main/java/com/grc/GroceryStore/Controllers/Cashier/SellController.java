package com.grc.GroceryStore.Controllers.Cashier;

import com.grc.GroceryStore.Models.*;
import com.grc.GroceryStore.Utils.CustomField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SellController implements Initializable {
    private final ObservableList<Category> categories = Model.getInstance().getStore().getCategories();
    private final ObservableList<ProductPoints> productPoints = Model.getInstance().getStore().getProductPoints();
    private final ObservableList<Discount> discounts = Model.getInstance().getStore().getDiscounts();
    private final ObservableList<Customer> customers = Model.getInstance().getStore().getCustomers();
    private final ObservableList<SellProduct> sellProducts = FXCollections.observableArrayList();
    private final ObservableList<Product> products = Model.getInstance().getStore().getProducts();
    private final ObservableList<CartProduct> cartProducts = FXCollections.observableArrayList();
    public ChoiceBox<Category> product_category_chb;
    public TableView<SellProduct> products_tbl;

    public Button add_btn;
    public Button clear_btn;
    public Button pay_btn;
    public Button remove_btn;
    public TableView<CartProduct> cart_tbl;
    public Label total_price_lb;
    public RadioButton cash_rd;
    public ToggleGroup payment_type;
    public ChoiceBox<Customer> customer_chb;
    public RadioButton cash_points_rd;
    public TextField amount_fld;
    public Label change_lb;
    public Label total_price_and_points_lb;
    public Button clear_customer_btn;
    double totalPrice = 0;
    double totalPriceWithPoints = 0;
    int totalPoints = 0;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (categories.isEmpty()) {
            categories.addAll(Category.getCategoriesDB());
        }

        product_category_chb.getItems().addAll(categories);
        if(!categories.isEmpty()){
            product_category_chb.setValue(categories.getFirst());
        }

        product_category_chb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                filterSellProductsByCategory();
            }
        });

        if (customers.isEmpty()) {
            customers.addAll(Customer.getCustomerList());
        }

        customer_chb.getItems().addAll(customers);

        products.clear();
        products.addAll(Product.getProductListDB());

        if (productPoints.isEmpty()) {
            productPoints.addAll(ProductPoints.getProductPointsDB());
        }

        if (discounts.isEmpty()) {
            discounts.addAll(Discount.getDiscountsDB());
        }

        filterSellProductsByCategory();

        products_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cart_tbl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        products_tbl.setItems(sellProducts);
        cart_tbl.setItems(cartProducts);

        cash_points_rd.setDisable(true);
        total_price_and_points_lb.setDisable(true);

        CustomField.doubleField(amount_fld);

        customer_chb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTotalLabels());

        add_btn.setOnAction(e -> onAdd());
        remove_btn.setOnAction(e -> onRemove());
        pay_btn.setOnAction(e -> onPay());
        clear_customer_btn.setOnAction(e -> customer_chb.getSelectionModel().clearSelection());
    }

    private void onPay(){
        if(cartProducts.isEmpty()){
            Model.showError("Error","No product in cart");
            return;
        }

        String amountText = amount_fld.getText();
        if(amountText.isEmpty()){
            Model.showError("Error", "Empty Amount");
            return;
        }

        if (!cash_points_rd.isSelected() && !cash_rd.isSelected()){
            Model.showError("Error", "You need to select a payment method");
            return;
        }

        double amount = Double.parseDouble(amountText);
        int userId = Model.getInstance().getUser().getId();

        if(cash_rd.isSelected()){
            if(totalPrice > amount){
                Model.showError("Error", "Not enough money.");
                return;
            }
            if (customer_chb.getSelectionModel().isEmpty()){
                Receipt receipt = Receipt.createReceiptDB(userId, totalPrice);
                if(receipt == null){
                    Model.showError("Error", "Could not create receipt.");
                    return;
                }
                boolean areCreated = SoldProduct.createSoldProductsCashDB(cartProducts, receipt.getId());
                if(!areCreated){
                    Model.showError("Error", "Could not create sold products.");
                    return;
                }

                setChange(amount - totalPrice);
                cartProducts.clear();
                return;
            }
            Customer customer = customer_chb.getValue();

            Receipt receipt = Receipt.createReceiptDB(customer.getId(), userId, totalPrice);
            if(receipt == null){
                Model.showError("Error", "Could not create receipt.");
                return;
            }

            boolean areCreated = SoldProduct.createSoldProductsCashDB(cartProducts, receipt.getId());
            if(!areCreated){
                Model.showError("Error", "Could not create sold products.");
                return;
            }

            double pointsPerEuro = Model.getInstance().getStore().getPointsPerEuro();
            int customerPoints = customer.getPoints();
            int points = (int) Math.floor(pointsPerEuro * totalPrice + customerPoints);

            Customer isUpdated = Customer.updateCustomerById(customer.getId(), customer.getName(), customer.getLastname(), customer.getPhoneNumber(), customer.getGender(), points);
            if(isUpdated == null){
                Model.showError("Error", "Could not update customer");
                return;
            }

            setChange(amount - totalPrice);
            cartProducts.clear();
            return;
        }

        if(cash_points_rd.isSelected()){
            if(totalPriceWithPoints > amount){
                Model.showError("Error", "Not enough money.");
                return;
            }
            if (customer_chb.getSelectionModel().isEmpty()){
                Model.showError("Error", "Please select a customer");
                return;
            }

            Customer customer = customer_chb.getValue();
            int customerPoints = customer.getPoints();
            if(customerPoints < totalPoints){
                Model.showError("Error", "Customer does not have enough points, it needs " + (totalPoints - customerPoints) + " more points.");
                return;
            }

            Receipt receipt = Receipt.createReceiptDB(customer.getId(), userId, totalPriceWithPoints);
            if(receipt == null){
                Model.showError("Error", "Could not create receipt.");
                return;
            }

            boolean areCreated = SoldProduct.createSoldProductsCashAndPointsDB(cartProducts, receipt.getId());
            if(!areCreated){
                Model.showError("Error", "Could not create sold products.");
                return;
            }

            double pointsPerEuro = Model.getInstance().getStore().getPointsPerEuro();
            int points = (int) Math.floor(pointsPerEuro * totalPriceWithPoints + customerPoints - totalPoints);

            Customer isUpdated = Customer.updateCustomerById(customer.getId(), customer.getName(), customer.getLastname(), customer.getPhoneNumber(), customer.getGender(), points);
            if(isUpdated == null){
                Model.showError("Error", "Could not update customer");
                return;
            }

            setChange(amount - totalPriceWithPoints);
            cartProducts.clear();
        }
    }

    void setChange(double difference){
        totalPrice = 0;
        totalPoints = 0;
        totalPriceWithPoints = 0;
        customer_chb.getSelectionModel().clearSelection();

        String formattedResult = formatMoneyString(difference);
        change_lb.setText(formattedResult);
    }

    private String formatMoneyString(double amount){
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(amount) + "€";
    }

    private void onAdd(){
        SellProduct selectedProduct = products_tbl.getSelectionModel().getSelectedItem();
        if (selectedProduct == null){
            Model.showError("Error", "You need to select a product to complete this action.");
            return;
        }

        if(selectedProduct.getStock() == 0){
            Model.showError("Error", "No product left.");
            return;
        }

        CartProduct cartProduct = findCartProductById(selectedProduct.getId());
        if(cartProduct == null){
            CartProduct newCartProduct = new CartProduct(selectedProduct);
            newCartProduct.stockProperty().set(newCartProduct.getStock() - 1);
            cartProducts.add(newCartProduct);
        } else {
            cartProduct.stockProperty().set(cartProduct.getStock() - 1);
            int qty = cartProduct.getQTY();
            cartProduct.QTYProperty().set(qty + 1);
        }

        selectedProduct.stockProperty().set(selectedProduct.getStock() - 1);

        Product product = Model.getInstance().getStore().findProductById(selectedProduct.getId());
        product.stockProperty().set(product.getStock() - 1);

        addTotalValues(selectedProduct);
    }

    private void onRemove(){
        CartProduct selectedProduct = cart_tbl.getSelectionModel().getSelectedItem();
        if (selectedProduct == null){
            Model.showError("Error", "You need to select a product to complete this action.");
            return;
        }

        SellProduct sellproduct = findSellProductById(selectedProduct.getId());
        if (sellproduct != null) sellproduct.stockProperty().set(sellproduct.getStock() + 1);

        Product product = Model.getInstance().getStore().findProductById(selectedProduct.getId());
        product.stockProperty().set(product.getStock() + 1);

        selectedProduct.stockProperty().set(selectedProduct.getStock() + 1);

        int qty = selectedProduct.getQTY();
        deleteTotalValues(selectedProduct);
        if(qty == 1){
            cartProducts.remove(selectedProduct);
            return;
        }

        selectedProduct.QTYProperty().set(qty - 1);
    }

    private CartProduct findCartProductById(int id){
        for (CartProduct cartProduct : cartProducts){
            if (cartProduct.getId() == id){
                return cartProduct;
            }
        }
        return null;
    }

    private SellProduct findSellProductById(int id){
        for (SellProduct sellProduct : sellProducts){
            if (sellProduct.getId() == id){
                return sellProduct;
            }
        }
        return null;
    }

    private void filterSellProductsByCategory(){
        sellProducts.clear();
        for (Product product : products) {
            Category selectedCategory = product_category_chb.getSelectionModel().getSelectedItem();
            if(selectedCategory.getId() != product.getCategory().getId()){
                continue;
            }

            Store store = Model.getInstance().getStore();
            int productId = product.getId();
            Discount discount = store.findDiscountByProductId(productId);
            ProductPoints points = store.findProductPointsByProductId(productId);
            SellProduct sellProduct = new SellProduct(product, discount, points);
            sellProducts.add(sellProduct);
        }
    }

    private void addTotalValues(SellProduct sellProduct) {
        double price = sellProduct.getPriceAfterDiscount();
        ProductPoints productPoints = sellProduct.getPoints();
        if(productPoints == null){
            totalPriceWithPoints += price;
        } else {
            totalPoints += productPoints.getPriceInPoints();
        }

        totalPrice += price;
        updateTotalLabels();
    }

    private void deleteTotalValues(CartProduct cartProduct) {
        double price = cartProduct.getPriceAfterDiscount();
        ProductPoints productPoints = cartProduct.getPoints();
        if(productPoints == null){
            totalPriceWithPoints -= price;
        } else {
            totalPoints -= productPoints.getPriceInPoints();
        }

        totalPrice -= price;
        updateTotalLabels();
    }

    private void updateTotalLabels(){
        if(totalPoints > 0 && !customer_chb.getSelectionModel().isEmpty()) {
            cash_points_rd.setDisable(false);
            total_price_and_points_lb.setDisable(false);
        } else {
            cash_points_rd.setDisable(true);
            total_price_and_points_lb.setDisable(true);
            cash_points_rd.setSelected(false);
        }
        total_price_lb.setText("Total Price: " + formatMoneyString(totalPrice));
        total_price_and_points_lb.setText("Total: " + formatMoneyString(totalPriceWithPoints) + " " + totalPoints + " Points");
    }
}