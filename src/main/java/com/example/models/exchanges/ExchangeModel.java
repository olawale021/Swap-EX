package com.example.models.exchanges;

import java.sql.Timestamp;

public class ExchangeModel {
    private int id;
    private int itemId;
    private int ownerId;
    private int interestedUserId;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String ownerUsername;
    private String interestedUserUsername;
    private String itemTitle;

    // Default constructor
    public ExchangeModel() {
    }

    // Constructor with all fields
    public ExchangeModel(int id, int itemId, int ownerId, int interestedUserId, String status, Timestamp createdAt, Timestamp updatedAt, String ownerUsername, String interestedUserUsername, String itemTitle) {
        this.id = id;
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.interestedUserId = interestedUserId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.ownerUsername = ownerUsername;
        this.interestedUserUsername = interestedUserUsername;
        this.itemTitle = itemTitle;
    }

    // Getters and setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getInterestedUserId() {
        return interestedUserId;
    }

    public void setInterestedUserId(int interestedUserId) {
        this.interestedUserId = interestedUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getInterestedUserUsername() {
        return interestedUserUsername;
    }

    public void setInterestedUserUsername(String interestedUserUsername) {
        this.interestedUserUsername = interestedUserUsername;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    // Method to get the name of the other user in the exchange
    public String getOtherUserUsername(int currentUserId) {
        return (currentUserId == ownerId) ? interestedUserUsername : ownerUsername;
    }

    // Method to get the ID of the other user in the exchange
    public int getOtherUserId(int currentUserId) {
        return (currentUserId == ownerId) ? interestedUserId : ownerId;
    }
}
