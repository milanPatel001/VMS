package com.project.projectVoucher.model;

import java.util.List;

public class AllowRequest {
    private String id;
    private String secretKey;
    private List<String> permissions;


    public AllowRequest(String id, String secretKey, List<String> permissions) {
        this.id = id;
        this.secretKey = secretKey;
        this.permissions = permissions;
    }


    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
