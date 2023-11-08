package com.project.projectVoucher.model;

public class AllowedUser {
    private String userId;
    private String authKey;

    public AllowedUser(String userId, String authKey) {
        this.userId = userId;
        this.authKey = authKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
