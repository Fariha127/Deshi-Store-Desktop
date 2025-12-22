package com.example.finding_bd_products;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;import java.time.format.DateTimeFormatter;import java.util.UUID;

public class ProductDetailsController {

    @FXML private Label productNameLabel;
    @FXML private Label productDescriptionLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productCategoryLabel;
    @FXML private Label recommendationCountLabel;
    @FXML private Label averageRatingLabel;
    @FXML private Button recommendButton;
    @FXML private TextArea reviewTextArea;
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextField userNameField;
    @FXML private Button submitReviewButton;
    @FXML private VBox reviewsContainer;
    @FXML private Button homeBtn;
    @FXML private Button categoriesBtn;
    @FXML private Button newlyAddedBtn;
    @FXML private Button favouritesBtn;
    @FXML private Button backButton;

    private Product currentProduct;
    private boolean hasRecommended = false;
    private DatabaseManager dbManager;

    public void initialize() {
        dbManager = DatabaseManager.getInstance();
        ratingComboBox.getItems().addAll(5, 4, 3, 2, 1);
        ratingComboBox.setValue(5);
    }

    public void setProduct(String productId) {
        this.currentProduct = dbManager.getProduct(productId);
        if (currentProduct != null) {
        }
    }

    public void setProduct(Product product) {
        this.currentProduct = product;
    }

    public static Product getProduct(String productId) {
        return DatabaseManager.getInstance().getProduct(productId);
    }

    private void displayProductDetails() {
        productNameLabel.setText(currentProduct.getName());
        productDescriptionLabel.setText(currentProduct.getDescription());
        productPriceLabel.setText("৳ " + (int)currentProduct.getPrice() + "/" + currentProduct.getUnit());
        productCategoryLabel.setText(currentProduct.getCategory());
        recommendationCountLabel.setText(currentProduct.getRecommendationCount() + " Recommendations");

        if (currentProduct.getReviews().isEmpty()) {
            averageRatingLabel.setText("No ratings yet");
        } else {
            averageRatingLabel.setText(String.format("%.1f ★ (%d reviews)",
                    currentProduct.getAverageRating(), currentProduct.getReviews().size()));
        }
    }



    private void loadReviews() {
        reviewsContainer.getChildren().clear();

        if (currentProduct.getReviews().isEmpty()) {
            Label noReviews = new Label("No reviews yet. Be the first to review this product!");
            noReviews.setStyle("-fx-font-size: 14px; -fx-text-fill: #888888; -fx-padding: 20;");
            reviewsContainer.getChildren().add(noReviews);
        } else {
            for (Review review : currentProduct.getReviews()) {
            }
        }
    }



    @FXML
    protected void onAddToFavourites() {
        // Add to database
        dbManager.addToFavourites(currentProduct.getProductId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added to Favourites");
        alert.setHeaderText(null);
        alert.setContentText(currentProduct.getName() + " added to favourites!");
        alert.showAndWait();
    }

    @FXML
    protected void onBack() {
        loadPage("Home.fxml");
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
        loadPage("NewProducts.fxml");
    }

    @FXML
    protected void showFavourites() {
        loadPage("MyFavouriteProducts.fxml");
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
