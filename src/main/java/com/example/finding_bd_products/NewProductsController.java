package com.example.finding_bd_products;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class NewProductsController {

    @FXML
    private GridPane productsGrid;

    @FXML
    private TextField searchField;

    @FXML
    private Button homeBtn;

    @FXML
    private Button categoriesBtn;

    @FXML
    private Button newlyAddedBtn;

    @FXML
    private Button favouritesBtn;

    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        productsGrid.getChildren().clear();

        java.util.List<Product> newProducts = new ArrayList<>();
        newProducts.add(ProductDetailsController.getProduct("speed"));
        newProducts.add(ProductDetailsController.getProduct("pran-frooto"));
        newProducts.add(ProductDetailsController.getProduct("lux-soap"));
        newProducts.add(ProductDetailsController.getProduct("closeup"));
        newProducts.add(ProductDetailsController.getProduct("radhuni"));
        newProducts.add(ProductDetailsController.getProduct("finis"));

        if (newProducts.isEmpty()) {
            Label noProducts = new Label("No new products available");
            noProducts.setStyle("-fx-font-size: 16px; -fx-text-fill: #888888;");
            productsGrid.add(noProducts, 0, 0);
            return;
        }

        int col = 0;
        int row = 0;

        for (Product product : newProducts) {
            if (product != null) {
                VBox productCard = createProductCard(product);
                productsGrid.add(productCard, col, row);

                col++;
                if (col > 2) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    @FXML
    protected void showHome() {
        loadPage("Home.fxml");
    }

    @FXML
    protected void showCategories() {
        loadPage("Categories.fxml");
    }

    @FXML
    protected void showNewlyAdded() {
        // Already on new products page
    }

    @FXML
    protected void showFavourites() {
        loadPage("MyFavouriteProducts.fxml");
    }

    @FXML
    protected void onSearch() {
        String searchText = searchField.getText();
        System.out.println("Searching for: " + searchText);
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) homeBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

