package com.example.models.contracts;

import java.sql.Timestamp;

public class ContractModel {
    private int id;
    private int exchangeId;
    private String terms;
    private boolean signedByOwner;
    private boolean signedByInterestedUser;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public ContractModel() {

    }

    // Parameterized constructor
    public ContractModel(int exchangeId, String terms, boolean signedByOwner, boolean signedByInterestedUser, Timestamp createdAt, Timestamp updatedAt) {
        this.exchangeId = exchangeId;
        this.terms = terms;
        this.signedByOwner = signedByOwner;
        this.signedByInterestedUser = signedByInterestedUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public boolean isSignedByOwner() {
        return signedByOwner;
    }

    public void setSignedByOwner(boolean signedByOwner) {
        this.signedByOwner = signedByOwner;
    }

    public boolean isSignedByInterestedUser() {
        return signedByInterestedUser;
    }

    public void setSignedByInterestedUser(boolean signedByInterestedUser) {
        this.signedByInterestedUser = signedByInterestedUser;
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
}
