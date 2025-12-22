package com.example.finding_bd_products;

import com.example.finding_bd_products.CategoryProductsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoriesController {
    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button homeBtn;

    @FXML
    private Button categoriesBtn;

    @FXML
    private Button newlyAddedBtn;

    @FXML
    private Button favouritesBtn;

    @FXML
    protected void showHome() {
        loadPage("Home.fxml");
    }

    @FXML
    protected void showCategories() {
    }

    @FXML
    protected void showNewlyAdded() {
        loadPage("NewlyAdded.fxml");
    }

    @FXML
    protected void showFavourites() {
        loadPage("Favourites.fxml");
    }

    @FXML
    protected void onSearch() {
        String searchText = searchField.getText();
    }

    @FXML
    public void showCategoryProducts(javafx.event.ActionEvent event) {
        VBox categoryCard = (VBox) ((javafx.scene.Node) event.getSource()).getParent();
        String categoryName = getCategoryNameFromCard(categoryCard);

        loadCategoryProducts(categoryName);
    }

    public void onCategoryClick(javafx.scene.input.MouseEvent event) {
        VBox categoryCard = (VBox) event.getSource();
        String categoryName = getCategoryNameFromCard(categoryCard);
        loadCategoryProducts(categoryName);
    }

    private String getCategoryNameFromCard(VBox categoryCard) {

        try {
            javafx.scene.control.Label nameLabel = (javafx.scene.control.Label) categoryCard.getChildren().get(1);
            return nameLabel.getText();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private void loadCategoryProducts(String categoryName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CategoryProducts.fxml"));
            Parent root = loader.load();

            CategoryProductsController controller = loader.getController();
            controller.setCategory(categoryName);

            Stage stage = (Stage) categoriesBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) categoriesBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
