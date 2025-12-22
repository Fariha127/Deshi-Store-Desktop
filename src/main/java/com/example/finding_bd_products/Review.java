package com.example.finding_bd_products;


import java.time.LocalDateTime;

public class Review {
    private String reviewId;
    private String productId;
    private String userName;
    private String comment;
    private int rating; // 1-5 stars
    private LocalDateTime datePosted;
    private boolean isVerifiedPurchase;

    public Review(String reviewId, String productId, String userName, String comment, int rating) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userName = userName;
        this.comment = comment;
        this.rating = rating;
        this.datePosted = LocalDateTime.now();
        this.isVerifiedPurchase = false;
    }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public LocalDateTime getDatePosted() { return datePosted; }
    public void setDatePosted(LocalDateTime datePosted) { this.datePosted = datePosted; }

    public boolean isVerifiedPurchase() { return isVerifiedPurchase; }
    public void setVerifiedPurchase(boolean verifiedPurchase) { isVerifiedPurchase = verifiedPurchase; }
}
