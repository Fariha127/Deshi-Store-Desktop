package com.example.finding_bd_products;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class VendorProductDetailsController {

    @FXML private ImageView productImageView;
    @FXML private TextField productIdField;
    @FXML private TextField productNameField;
    @FXML private TextField manufacturerField;
    @FXML private TextField categoryField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField priceField;
    @FXML private TextField unitField;
    @FXML private TextField statusField;
    @FXML private VBox rejectionReasonBox;
    @FXML private TextArea rejectionReasonArea;
    @FXML private Button editBtn;

    private Product product;

    public void setProduct(Product product) {
        this.product = product;
        loadProductDetails();
    }

    private void loadProductDetails() {
        if (product == null) return;

        // Load image
        try {
            String imagePath = product.getImageUrl();
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                productImageView.setImage(image);
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        // Set text fields
        productIdField.setText(product.getProductId());
        productNameField.setText(product.getName());
        manufacturerField.setText(product.getManufacturerName() != null ? product.getManufacturerName() : "N/A");
        categoryField.setText(product.getCategory());
        descriptionArea.setText(product.getDescription());
        priceField.setText(String.format("%.2f", product.getPrice()));
        unitField.setText(product.getUnit() != null ? product.getUnit() : "N/A");

        // Set approval status with color
        String status = product.getApprovalStatus();
        if (status != null) {
            statusField.setText(status.toUpperCase());
            if ("approved".equalsIgnoreCase(status)) {
                statusField.setStyle("-fx-background-color: #C8E6C9; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
            } else if ("rejected".equalsIgnoreCase(status)) {
                statusField.setStyle("-fx-background-color: #ffcdd2; -fx-text-fill: #c62828; -fx-font-weight: bold;");
            } else {
                statusField.setStyle("-fx-background-color: #FFE0B2; -fx-text-fill: #E65100; -fx-font-weight: bold;");
            }
        }

        // Show rejection reason if rejected
        String rejectionReason = product.getRejectionReason();
        if (rejectionReason != null && !rejectionReason.isEmpty()) {
            rejectionReasonBox.setVisible(true);
            rejectionReasonBox.setManaged(true);
            rejectionReasonArea.setText(rejectionReason);
        }

        // Disable edit button if product is rejected
        if ("rejected".equalsIgnoreCase(status)) {
            editBtn.setDisable(false); // Allow editing to fix issues
            editBtn.setText("Edit & Resubmit");
        }
    }

    @FXML
    private void handleEditProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditProduct.fxml"));
            Parent root = loader.load();

            EditProductController controller = loader.getController();
            controller.setProduct(product);

            Stage stage = (Stage) productIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Edit Product");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit page: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VendorDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) productIdField.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Vendor Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to go back: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
