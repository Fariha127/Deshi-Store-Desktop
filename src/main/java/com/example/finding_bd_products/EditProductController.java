package com.example.finding_bd_products;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class EditProductController {

    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private TextField manufacturerField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private TextField priceField;
    @FXML private TextField volumeField;
    @FXML private TextField imagePathField;
    @FXML private ImageView imagePreview;
    @FXML private Label messageLabel;

    private DatabaseManager dbManager;
    private Product product;
    private String currentImagePath;
    private boolean imageChanged = false;

    @FXML
    public void initialize() {
        dbManager = DatabaseManager.getInstance();
        setupCategories();
    }

    private void setupCategories() {
        categoryComboBox.getItems().addAll(
                "Fruits & Vegetables",
                "Dairy & Eggs",
                "Meat & Seafood",
                "Bakery & Bread",
                "Rice & Grains",
                "Oil & Spices",
                "Snacks & Beverages",
                "Personal Care",
                "Household Items",
                "Others"
        );
    }

    public void setProduct(Product product) {
        this.product = product;
        this.currentImagePath = product.getImageUrl();
        loadProductData();
    }

    private void loadProductData() {
        if (product == null) return;

        productIdField.setText(product.getProductId());
        productNameField.setText(product.getName());
        manufacturerField.setText(product.getManufacturerName() != null ? product.getManufacturerName() : "");
        categoryComboBox.setValue(product.getCategory());
        descriptionArea.setText(product.getDescription());
        priceField.setText(String.format("%.2f", product.getPrice()));
        volumeField.setText(product.getUnit() != null ? product.getUnit() : "");
        imagePathField.setText(product.getImageUrl() != null ? product.getImageUrl() : "");

        // Load current image
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(product.getImageUrl()));
                imagePreview.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading current image: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) productNameField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Generate unique filename
                String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                // Copy image to resources/images directory
                Path sourcePath = selectedFile.toPath();
                Path targetPath = Paths.get("src/main/resources/images/" + uniqueFileName);

                // Create directory if it doesn't exist
                Files.createDirectories(targetPath.getParent());

                // Copy file
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Store path relative to resources
                currentImagePath = "/images/" + uniqueFileName;
                imagePathField.setText(currentImagePath);
                imageChanged = true;

                // Load and display preview
                Image image = new Image(selectedFile.toURI().toString());
                imagePreview.setImage(image);

                messageLabel.setText("");
            } catch (IOException e) {
                messageLabel.setText("Error uploading image: " + e.getMessage());
                messageLabel.setStyle("-fx-text-fill: #f44336;");
            }
        }
    }

    @FXML
    private void handleUpdateProduct() {
        // Validate inputs
        String name = productNameField.getText().trim();
        String category = categoryComboBox.getValue();
        String description = descriptionArea.getText().trim();
        String priceText = priceField.getText().trim();
        String volume = volumeField.getText().trim();

        if (name.isEmpty() || category == null || description.isEmpty() || priceText.isEmpty() || volume.isEmpty()) {
            showMessage("Please fill in all required fields!", "#f44336");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                showMessage("Price must be greater than 0!", "#f44336");
                return;
            }
        } catch (NumberFormatException e) {
            showMessage("Invalid price format!", "#f44336");
            return;
        }

        // If image was changed or doesn't exist, require an image
        if (currentImagePath == null || currentImagePath.isEmpty()) {
            showMessage("Please select a product image!", "#f44336");
            return;
        }

        // Update product in database
        boolean success = dbManager.updateProduct(
                product.getProductId(),
                name,
                description,
                price,
                volume,
                category,
                currentImagePath
        );

        if (success) {
            showMessage("Product updated successfully! Waiting for admin approval.", "#4CAF50");
            
            // Wait a moment then go back to dashboard (since edited version is now separate)
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::goBackToDashboard);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showMessage("Failed to update product. Please try again.", "#f44336");
        }
    }

    @FXML
    private void handleCancel() {
        goBackToProductDetails();
    }

    @FXML
    private void handleBackToDetails() {
        goBackToProductDetails();
    }

    private void goBackToProductDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VendorProductDetails.fxml"));
            Parent root = loader.load();

            // Reload the product with updated data
            Product updatedProduct = dbManager.getProductById(product.getProductId());
            VendorProductDetailsController controller = loader.getController();
            controller.setProduct(updatedProduct != null ? updatedProduct : product);

            Stage stage = (Stage) productIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error navigating back: " + e.getMessage(), "#f44336");
        }
    }

    private void goBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VendorDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) productIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Vendor Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error navigating back: " + e.getMessage(), "#f44336");
        }
    }

    private void showMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + ";");
    }
}
