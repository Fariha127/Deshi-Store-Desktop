package com.example.finding_bd_products;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VendorProductsListController {

    @FXML private Button backToDashboardBtn;
    @FXML private Button addProductBtn;
    @FXML private Button logoutBtn;

    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, String> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, String> productCategoryColumn;
    @FXML private TableColumn<Product, String> productPriceColumn;
    @FXML private TableColumn<Product, String> productStatusColumn;
    @FXML private TableColumn<Product, String> productReasonColumn;

    private DatabaseManager dbManager;

    @FXML
    public void initialize() {
        dbManager = DatabaseManager.getInstance();
        setupProductsTable();
        loadProducts();
    }

    private void setupProductsTable() {
        productIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductId()));
        productNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        productCategoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        productPriceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().getPrice())));
        
        // Status column with color coding
        productStatusColumn.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Product product = getTableView().getItems().get(getIndex());
                    String status = product.getApprovalStatus();
                    setText(status != null ? status.toUpperCase() : "PENDING");
                    
                    if ("approved".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                    } else if ("rejected".equalsIgnoreCase(status)) {
                        setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;");
                    }
                }
            }
        });

        productStatusColumn.setCellValueFactory(data -> {
            String status = data.getValue().getApprovalStatus();
            return new SimpleStringProperty(status != null ? status : "pending");
        });

        // Rejection reason column
        productReasonColumn.setCellValueFactory(data -> {
            String reason = data.getValue().getRejectionReason();
            return new SimpleStringProperty(reason != null ? reason : "-");
        });

        productReasonColumn.setCellFactory(column -> new TableCell<Product, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || "-".equals(item)) {
                    setText("-");
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #f44336;");
                    setWrapText(true);
                }
            }
        });

        // Add double-click to view product details
        productsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
                if (selectedProduct != null) {
                    showProductDetails(selectedProduct);
                }
            }
        });
    }

    private void showProductDetails(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VendorProductDetails.fxml"));
            Parent root = loader.load();

            VendorProductDetailsController controller = loader.getController();
            controller.setProduct(product);

            Stage stage = (Stage) productsTable.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        String vendorId = VendorSession.getInstance().getCurrentVendorId();
        if (vendorId != null) {
            List<Product> products = dbManager.getProductsByVendor(vendorId);
            ObservableList<Product> productsList = FXCollections.observableArrayList(products);
            productsTable.setItems(productsList);
        }
    }

    @FXML
    private void refreshProducts() {
        loadProducts();
    }

    @FXML
    private void goBackToDashboard() {
        loadPage("VendorDashboard.fxml");
    }

    @FXML
    private void goToAddProduct() {
        if (!VendorSession.getInstance().isLoggedIn()) {
            showAlert(Alert.AlertType.WARNING, "Access Denied", "Only vendors can add products.");
            return;
        }
        
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
            stage.setTitle("Add Product");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
