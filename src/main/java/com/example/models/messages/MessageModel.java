package com.example.models.messages;

import java.sql.Timestamp;

public class MessageModel {
    private int id;
    private int exchangeId;
    private int senderId;
    private int receiverId;
    private String content;
    private Timestamp createdAt;

    // Default constructor
    public MessageModel() {
    }

    // Constructor with all fields
    public MessageModel(int id, int exchangeId, int senderId, int receiverId, String content, Timestamp createdAt) {
        this.id = id;
        this.exchangeId = exchangeId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
