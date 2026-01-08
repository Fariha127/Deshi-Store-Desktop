package com.example.finding_bd_products;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MyProfileController {
    @FXML
    private TextField fullNameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private Button sendCodeBtn;
    
    @FXML
    private VBox verificationCodeBox;
    
    @FXML
    private TextField verificationCodeField;
    
    @FXML
    private Button verifyBtn;
    
    @FXML
    private Label verificationStatusLabel;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private TextField dobField;
    
    @FXML
    private TextField genderField;
    
    @FXML
    private ComboBox<String> genderComboBox;
    
    @FXML
    private TextField cityField;
    
    @FXML
    private Label changePasswordLabel;
    
    @FXML
    private VBox currentPasswordBox;
    
    @FXML
    private VBox newPasswordBox;
    
    @FXML
    private VBox confirmPasswordBox;
    
    @FXML
    private PasswordField currentPasswordField;
    
    @FXML
    private PasswordField newPasswordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Button editBtn;
    
    @FXML
    private Button saveBtn;
    
    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button logoutBtn;
    
    @FXML
    private Button myProductsBtn;
    
    private DatabaseManager dbManager;
    private EmailService emailService;
    private User currentUser;
    private boolean isEditMode = false;
    private String generatedVerificationCode;
    private long verificationCodeTimestamp;
    private String pendingNewEmail;
    private boolean emailVerified = false;
    private String originalEmail;
    
    @FXML
    public void initialize() {
        dbManager = DatabaseManager.getInstance();
        emailService = EmailService.getInstance();
        currentUser = UserSession.getInstance().getCurrentUser();
        
        // Setup gender combo box
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        
        // Show "My Products" button only for logged-in vendors
        if (myProductsBtn != null && VendorSession.getInstance().isLoggedIn()) {
            myProductsBtn.setVisible(true);
            myProductsBtn.setManaged(true);
        }
        
        // Load user data
        if (currentUser != null) {
            loadUserData();
            setViewMode();
        } else {
            // If no user logged in, redirect to login
            goToLogin();
        }
    }
    
    private void loadUserData() {
        fullNameField.setText(currentUser.getFullName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhoneNumber());
        dobField.setText(currentUser.getDateOfBirth());
        genderField.setText(currentUser.getGender());
        genderComboBox.setValue(currentUser.getGender());
        cityField.setText(currentUser.getCity());
    }
    
    private void setViewMode() {
        isEditMode = false;
        emailVerified = false;
        originalEmail = currentUser.getEmail();
        
        // Make fields read-only
        fullNameField.setEditable(false);
        emailField.setEditable(false);
        phoneField.setEditable(false);
        dobField.setEditable(false);
        cityField.setEditable(false);
        
        // Show genderField (read-only text), hide genderComboBox
        genderField.setVisible(true);
        genderField.setManaged(true);
        genderComboBox.setVisible(false);
        genderComboBox.setManaged(false);
        
        // Set gray background for read-only fields
        String readOnlyStyle = "-fx-background-color: #F0F0F0; -fx-background-radius: 5; -fx-font-size: 14px; -fx-padding: 10; -fx-opacity: 1;";
        fullNameField.setStyle(readOnlyStyle);
        emailField.setStyle(readOnlyStyle);
        phoneField.setStyle(readOnlyStyle);
        dobField.setStyle(readOnlyStyle);
        cityField.setStyle(readOnlyStyle);
        
        // Hide email verification elements
        sendCodeBtn.setVisible(false);
        sendCodeBtn.setManaged(false);
        verificationCodeBox.setVisible(false);
        verificationCodeBox.setManaged(false);
        
        // Hide password fields
        changePasswordLabel.setVisible(false);
        changePasswordLabel.setManaged(false);
        currentPasswordBox.setVisible(false);
        currentPasswordBox.setManaged(false);
        newPasswordBox.setVisible(false);
        newPasswordBox.setManaged(false);
        confirmPasswordBox.setVisible(false);
        confirmPasswordBox.setManaged(false);
        
        // Show Edit button, hide Save and Cancel buttons
        editBtn.setVisible(true);
        editBtn.setManaged(true);
        saveBtn.setVisible(false);
        saveBtn.setManaged(false);
        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);
    }
    
    private void setEditMode() {
        isEditMode = true;
        originalEmail = currentUser.getEmail();
        emailVerified = false;
        
        // Make fields editable
        fullNameField.setEditable(true);
        emailField.setEditable(true);
        phoneField.setEditable(true);
        dobField.setEditable(true);
        cityField.setEditable(true);
        
        // Hide genderField, show genderComboBox for editing
        genderField.setVisible(false);
        genderField.setManaged(false);
        genderComboBox.setVisible(true);
        genderComboBox.setManaged(true);
        
        // Set green background for editable fields
        String editableStyle = "-fx-background-color: #C9F6C1; -fx-background-radius: 5; -fx-font-size: 14px; -fx-padding: 10;";
        fullNameField.setStyle(editableStyle);
        emailField.setStyle(editableStyle);
        phoneField.setStyle(editableStyle);
        dobField.setStyle(editableStyle);
        cityField.setStyle(editableStyle);
        
        // Show send code button
        sendCodeBtn.setVisible(true);
        sendCodeBtn.setManaged(true);
        
        // Show password fields
        changePasswordLabel.setVisible(true);
        changePasswordLabel.setManaged(true);
        currentPasswordBox.setVisible(true);
        currentPasswordBox.setManaged(true);
        newPasswordBox.setVisible(true);
        newPasswordBox.setManaged(true);
        confirmPasswordBox.setVisible(true);
        confirmPasswordBox.setManaged(true);
        
        // Hide Edit button, show Save and Cancel buttons
        editBtn.setVisible(false);
        editBtn.setManaged(false);
        saveBtn.setVisible(true);
        saveBtn.setManaged(true);
        cancelBtn.setVisible(true);
        cancelBtn.setManaged(true);
    }
    
    @FXML
    protected void handleEditProfile() {
        setEditMode();
    }
    
    @FXML
    protected void handleSaveProfile() {
        // Validate required fields
        if (fullNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Full name is required", Alert.AlertType.ERROR);
            return;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            showAlert("Error", "Phone number is required", Alert.AlertType.ERROR);
            return;
        }
        
        // Check if email has changed
        String newEmail = emailField.getText().trim();
        if (!newEmail.equals(originalEmail)) {
            if (!emailVerified) {
                showAlert("Error", "Please verify your new email address before saving", Alert.AlertType.ERROR);
                return;
            }
        }
        
        // Check if password change is requested
        boolean changePassword = false;
        String newPassword = null;
        
        if (!currentPasswordField.getText().isEmpty() || !newPasswordField.getText().isEmpty() || !confirmPasswordField.getText().isEmpty()) {
            // Validate password change
            if (currentPasswordField.getText().isEmpty()) {
                showAlert("Error", "Please enter your current password", Alert.AlertType.ERROR);
                return;
            }
            
            if (!currentPasswordField.getText().equals(currentUser.getPassword())) {
                showAlert("Error", "Current password is incorrect", Alert.AlertType.ERROR);
                return;
            }
            
            if (newPasswordField.getText().isEmpty()) {
                showAlert("Error", "Please enter a new password", Alert.AlertType.ERROR);
                return;
            }
            
            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                showAlert("Error", "New passwords do not match", Alert.AlertType.ERROR);
                return;
            }
            
            if (newPasswordField.getText().length() < 6) {
                showAlert("Error", "New password must be at least 6 characters", Alert.AlertType.ERROR);
                return;
            }
            
            changePassword = true;
            newPassword = newPasswordField.getText();
        }
        
        // Update user object
        currentUser.setFullName(fullNameField.getText().trim());
        currentUser.setEmail(emailField.getText().trim());
        currentUser.setPhoneNumber(phoneField.getText().trim());
        currentUser.setDateOfBirth(dobField.getText().trim());
        currentUser.setGender(genderComboBox.getValue());
        currentUser.setCity(cityField.getText().trim());
        
        if (changePassword) {
            currentUser.setPassword(newPassword);
        }
        
        // Update in database
        boolean success = dbManager.updateUserProfile(currentUser);
        
        if (success) {
            // Update session with new data
            UserSession.getInstance().login(currentUser);
            
            showAlert("Success", "Profile updated successfully!", Alert.AlertType.INFORMATION);
            
            // Clear password fields
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
            
            // Return to view mode
            setViewMode();
        } else {
            showAlert("Error", "Failed to update profile. Please try again.", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    protected void handleCancel() {
        // Reload user data and return to view mode
        loadUserData();
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        verificationCodeField.clear();
        emailVerified = false;
        setViewMode();
    }
    
    @FXML
    protected void handleSendVerificationCode() {
        String newEmail = emailField.getText().trim();
        
        // Validate email format
        if (newEmail.isEmpty() || !newEmail.contains("@")) {
            showAlert("Error", "Please enter a valid email address", Alert.AlertType.ERROR);
            return;
        }
        
        // Check if email has actually changed
        if (newEmail.equals(originalEmail)) {
            showAlert("Info", "This is your current email address", Alert.AlertType.INFORMATION);
            return;
        }
        
        // Check if email already exists in database
        if (dbManager.emailExists(newEmail)) {
            showAlert("Error", "This email is already registered to another account", Alert.AlertType.ERROR);
            return;
        }
        
        // Generate and send verification code
        generatedVerificationCode = emailService.generateVerificationCode();
        verificationCodeTimestamp = System.currentTimeMillis();
        pendingNewEmail = newEmail;
        
        boolean emailSent = emailService.sendVerificationEmail(newEmail, generatedVerificationCode);
        
        if (emailSent) {
            verificationCodeBox.setVisible(true);
            verificationCodeBox.setManaged(true);
            verificationStatusLabel.setText("Verification code sent to " + newEmail);
            verificationStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4CAF50;");
            showAlert("Success", "Verification code sent to " + newEmail, Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Failed to send verification email. Please check the email address and try again.", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    protected void handleVerifyCode() {
        String enteredCode = verificationCodeField.getText().trim();
        
        if (enteredCode.isEmpty()) {
            showAlert("Error", "Please enter the verification code", Alert.AlertType.ERROR);
            return;
        }
        
        // Check if code has expired (10 minutes)
        long currentTime = System.currentTimeMillis();
        long elapsedMinutes = (currentTime - verificationCodeTimestamp) / 1000 / 60;
        
        if (elapsedMinutes > 10) {
            showAlert("Error", "Verification code has expired. Please request a new code.", Alert.AlertType.ERROR);
            verificationCodeField.clear();
            return;
        }
        
        // Verify the code
        if (enteredCode.equals(generatedVerificationCode)) {
            emailVerified = true;
            verificationStatusLabel.setText("✓ Email verified successfully!");
            verificationStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
            verificationCodeField.setEditable(false);
            verifyBtn.setDisable(true);
            emailField.setEditable(false);
            sendCodeBtn.setDisable(true);
            showAlert("Success", "Email verified successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Invalid verification code. Please try again.", Alert.AlertType.ERROR);
            verificationCodeField.clear();
        }
    }
    
    @FXML
    protected void goToHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    protected void goToMyProducts() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("VendorProductsList.fxml"));
            Stage stage = (Stage) myProductsBtn.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    protected void handleLogout() {
        try {
            UserSession.getInstance().logout();
            VendorSession.getInstance().logout();
            
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
