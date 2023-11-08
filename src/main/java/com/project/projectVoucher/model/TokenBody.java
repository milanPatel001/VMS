package com.project.projectVoucher.model;

public class TokenBody {
    private String apiKey;
    private String id;

    public TokenBody() {
    }

    public TokenBody(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
