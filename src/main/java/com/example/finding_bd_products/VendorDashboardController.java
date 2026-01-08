package com.example.finding_bd_products;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VendorDashboardController {

    @FXML
    private Button addProductBtn;

    @FXML
    private Button myProductsBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private GridPane productsGrid;

    @FXML
    private javafx.scene.control.TextField searchField;

    private DatabaseManager dbManager;
    private List<Product> allApprovedProducts;

    @FXML
    public void initialize() {
        dbManager = DatabaseManager.getInstance();
        if (productsGrid != null) {
            loadApprovedProducts();
        }
        
        // Setup search listener
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterProducts(newValue);
            });
        }
    }

    private void loadApprovedProducts() {
        productsGrid.getChildren().clear();

        String vendorId = VendorSession.getInstance().getCurrentVendorId();
        if (vendorId == null) {
            System.out.println("No vendor logged in");
            return;
        }

        // Get all products by this vendor
        List<Product> allProducts = dbManager.getProductsByVendor(vendorId);
        
        // Only show approved products that are not pending edits (original_product_id IS NULL or empty)
        allApprovedProducts = allProducts.stream()
                .filter(p -> "approved".equalsIgnoreCase(p.getApprovalStatus()) &&
                            (p.getOriginalProductId() == null || p.getOriginalProductId().isEmpty()))
                .toList();

        displayProducts(allApprovedProducts);
    }

    private void displayProducts(List<Product> products) {
        productsGrid.getChildren().clear();

        if (products.isEmpty()) {
            Label emptyLabel = new Label("No products found.");
            emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");
            productsGrid.add(emptyLabel, 0, 0, 3, 1);
            GridPane.setMargin(emptyLabel, new Insets(50));
            return;
        }

        int col = 0;
        int row = 0;

        for (Product product : products) {
            if (row >= 4) break; // Limit to 4 rows (12 products)

            VBox productCard = createProductCard(product);
            productsGrid.add(productCard, col, row);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        card.setPrefWidth(280);
        card.setPrefHeight(350);
        card.setMaxWidth(280);
        card.setMaxHeight(350);

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(10);
        shadow.setOffsetY(3);
        card.setEffect(shadow);

        // Product Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(250);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        try {
            String imagePath = product.getImageUrl();
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                imageView.setImage(image);
            } else {
                Image placeholderImage = new Image(getClass().getResourceAsStream("/images/placeholder.png"));
                imageView.setImage(placeholderImage);
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            try {
                Image placeholderImage = new Image(getClass().getResourceAsStream("/images/placeholder.png"));
                imageView.setImage(placeholderImage);
            } catch (Exception ex) {
                // If placeholder also fails, just show empty ImageView
            }
        }

        // Product Name
        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("System Bold", 16));
        nameLabel.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(250);
        nameLabel.setAlignment(Pos.CENTER);

        // Product Category
        Label categoryLabel = new Label(product.getCategory());
        categoryLabel.setFont(Font.font(12));
        categoryLabel.setStyle("-fx-text-fill: #666;");

        // Product Price
        Label priceLabel = new Label("৳ " + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(Font.font("System Bold", 18));
        priceLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");

        // View Details Button
        Button detailsBtn = new Button("View Details");
        detailsBtn.setStyle("-fx-background-color: linear-gradient(to bottom, #2E7D32, #4CAF50); " +
                "-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; " +
                "-fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 8 20 8 20;");
        detailsBtn.setOnAction(e -> showProductDetails(product));

        card.getChildren().addAll(imageView, nameLabel, categoryLabel, priceLabel, detailsBtn);

        // Make card clickable
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 10; -fx-padding: 15; -fx-cursor: hand;");
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        });

        return card;
    }

    private void showProductDetails(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VendorProductDetails.fxml"));
            Parent root = loader.load();

            VendorProductDetailsController controller = loader.getController();
            controller.setProduct(product);

            Stage stage = (Stage) productsGrid.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAddProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            Parent root = loader.load();

            AddProductController controller = loader.getController();
            controller.setVendorInfo(
                    VendorSession.getInstance().getCurrentVendorId(),
                    VendorSession.getInstance().getVendorType()
            );

            Stage stage = (Stage) addProductBtn.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToMyProducts() {
        loadPage("VendorProductsList.fxml");
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        VendorSession.getInstance().logout();
        loadPage("Login.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) addProductBtn.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterProducts(String searchText) {
        if (allApprovedProducts == null) {
            return;
        }

        if (searchText == null || searchText.trim().isEmpty()) {
            displayProducts(allApprovedProducts);
            return;
        }

        String lowerSearchText = searchText.toLowerCase().trim();
        List<Product> filteredProducts = allApprovedProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerSearchText) ||
                            p.getCategory().toLowerCase().contains(lowerSearchText) ||
                            p.getProductId().toLowerCase().contains(lowerSearchText))
                .toList();

        displayProducts(filteredProducts);
    }

    @FXML
    private void handleClearSearch() {
        if (searchField != null) {
            searchField.clear();
        }
    }

    @FXML
    private void handleSearch() {
        if (searchField != null) {
            filterProducts(searchField.getText());
        }
    }
}
