package com.example.models.offers;

import org.json.JSONObject;
import java.sql.Timestamp;

public class OfferModel {
    private int id;
    private int itemId;
    private int userId;
    private JSONObject offerDetails;
    private Timestamp createdAt;

    // Constructors
    public OfferModel() {}

    public OfferModel(int id, int itemId, int userId, JSONObject offerDetails, Timestamp createdAt) {
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
        this.offerDetails = offerDetails;
        this.createdAt = createdAt;
    }

    // Getters and Setters
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public JSONObject getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(JSONObject offerDetails) {
        this.offerDetails = offerDetails;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
