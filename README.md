# 🛍️ Deshi Store Desktop (Finding BD Products)

![Java](https://img.shields.io/badge/Java-25%20%2F%2021-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue.svg)
![Database](https://img.shields.io/badge/SQLite-3.47.1.0-lightgrey.svg)
![Build](https://img.shields.io/badge/Maven-3.8%2B-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

**Deshi Store Desktop** is a modern cross-platform desktop application built using **JavaFX** and **SQLite**, designed to highlight, categorize, and promote authentic Bangladeshi products (*Deshi Products*). The application connects consumers with authentic Bangladeshi manufacturers, company vendors, and retail shop owners through a unified, role-based platform.

---

## 🌟 Key Features

### 🛒 Consumer / User Features
* **Product Catalog Browsing**: Explore products by category (Food & Grocery, Beverages, Snacks, Dairy, Skincare, Haircare, Oral Care, Baby Care, Home Care, and more).
* **Search & Filtering**: Search products by name, vendor, or category.
* **Product Details & Reviews**: View detailed product specifications, images, average ratings, customer reviews, and recommendation counts.
* **Review & Rating System**: Registered users can submit reviews and ratings for verified products.
* **Favourites Management**: Bookmark favorite products and track preferred product categories.
* **User Profile**: Personal dashboard allowing users to manage account details, profile picture, and saved items.

### 🏭 Vendor Features (Company & Retail)
* **Dual Vendor Onboarding**:
  * **Company Vendors**: Requires corporate credentials including Company Name, Registration Number, BSTI Certificate Number, Address, and TIN.
  * **Retail Vendors**: Designed for local shop owners, requiring Shop Name, Trade License Number, Address, and TIN.
* **Vendor Dashboard**: Dedicated dashboard to track catalog statistics and product performance.
* **Product Submission Workflow**: Add new products with image uploads, detailed pricing, descriptions, and categories.
* **Approval Status Tracking**: Real-time tracking of product listings (`Pending`, `Approved`, `Rejected`).
* **Instant Notifications**: Receive feedback and rejection reasons directly from platform admins.

### 🛡️ Administrator Features
* **Admin Dashboard**: Overview of system statistics, vendor applications, and pending product submissions.
* **Vendor Approval System**: Review and verify business registrations for company and retail vendors.
* **Product Moderation**: Inspect, approve, or reject vendor-submitted products with feedback comments.
* **User Management**: Monitor user registrations and manage platform activity.

### 📧 Automated Email Notifications
* Integrated JavaMail (`javax.mail`) service to deliver automated updates regarding vendor application approvals, product submission status changes, and platform notifications.

---

## 🛠️ Technology Stack & Libraries

* **Core Language**: Java 25 (Compatible with Java 21 LTS+)
* **UI Framework**: [JavaFX 21.0.6](https://openjfx.io/)
* **Database**: [SQLite JDBC 3.47.1.0](https://github.com/xerial/sqlite-jdbc)
* **UI Component Libraries**:
  * **ControlsFX**: Advanced UI controls and dialogs.
  * **BootstrapFX**: Bootstrap-style styling for JavaFX components.
  * **TilesFX**: Graphical tiles and dashboard metrics.
  * **Ikonli**: Icon fonts for JavaFX views.
  * **FormsFX / ValidatorFX**: Form design and real-time input validation.
* **Email Service**: JavaMail API (`com.sun.mail:javax.mail:1.6.2`)
* **Build System**: Apache Maven

---

## 📁 Architecture & Project Structure

The project follows the Model-View-Controller (MVC) architecture pattern:

```
Deshi-Store-Desktop/
├── src/
│   └── main/
│       ├── java/
│       │   ├── module-info.java                   # Java Module System declaration
│       │   └── com/example/finding_bd_products/
│       │       ├── HelloApplication.java          # JavaFX Application Launcher
│       │       ├── Launcher.java                  # Main Entry point
│       │       ├── DatabaseManager.java           # SQLite DAO & Connection Manager
│       │       ├── EmailService.java              # JavaMail Email Dispatcher
│       │       │
│       │       ├── User.java                      # User Entity Model
│       │       ├── CompanyVendor.java             # Company Vendor Entity Model
│       │       ├── RetailVendor.java              # Retail Vendor Entity Model
│       │       ├── Product.java                   # Product Entity Model
│       │       ├── Review.java                    # Review Entity Model
│       │       ├── UserSession.java / VendorSession.java # Active Sessions
│       │       │
│       │       ├── HomeController.java            # Home Screen Controller
│       │       ├── AllProductsController.java     # Product List Controller
│       │       ├── ProductDetailsController.java  # Single Product View Controller
│       │       ├── AdminDashboardController.java  # Admin Panel Controller
│       │       ├── VendorDashboardController.java # Vendor Panel Controller
│       │       └── [Other Controllers]...
│       │
│       └── resources/
│           ├── com/example/finding_bd_products/   # FXML Layout Views
│           │   ├── Home.fxml
│           │   ├── ProductDetails.fxml
│           │   ├── AdminDashboard.fxml
│           │   ├── VendorDashboard.fxml
│           │   └── [Other FXML Files]...
│           └── images/                            # Product & Category Image Assets
│
├── bd_products.db                                 # Embedded SQLite Database
└── pom.xml                                        # Maven Build Configuration
```

---

## 🗄️ Database Schema Overview

The embedded SQLite database (`bd_products.db`) contains the following primary tables:

| Table | Description |
| :--- | :--- |
| `users` | Consumer profiles, credentials, and contact details |
| `company_vendors` | Company vendor profiles, BSTI credentials, and approval status |
| `retail_vendors` | Retail vendor profiles, trade license information, and approval status |
| `admins` | Platform administrative credentials |
| `products` | Product catalog, pricing, vendor assignment, and moderation status |
| `reviews` | User reviews and ratings linked to products |
| `favourites` | User bookmarked products |
| `favourite_categories` | User saved categories |
| `notifications` | System & admin feedback notifications for vendors |

---

## 🚀 Getting Started

### Prerequisites

* **Java Development Kit (JDK)**: JDK 21 or higher installed and configured in your `PATH`.
* **Maven**: Apache Maven 3.8+ (or use the included Maven wrapper `./mvnw`).

### Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Fariha127/Deshi-Store-Desktop.git
   cd Deshi-Store-Desktop
   ```

2. **Build the project**:
   ```bash
   ./mvnw clean compile
   ```
   *(On Windows Command Prompt: `mvnw.cmd clean compile`)*

3. **Run the Application**:
   ```bash
   ./mvnw javafx:run
   ```
   *(Or launch the `Launcher.java` / `HelloApplication.java` directly from IntelliJ IDEA or Eclipse)*

---

## 📜 Usage Workflows

1. **First-time Launch**: The application auto-initializes the `bd_products.db` SQLite database if it does not exist.
2. **Browsing as Guest/User**: Launch the home screen to browse featured Bangladeshi products, search catalog items, or view details.
3. **Signing Up as Vendor**: Navigate to vendor registration, choose **Company Vendor** or **Retail Vendor**, fill in registration details, and submit for admin approval.
4. **Admin Approval**: Admins log in to review pending vendor applications and approve product catalog submissions before they appear publicly.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
