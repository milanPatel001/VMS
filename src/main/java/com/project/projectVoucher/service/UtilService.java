package com.project.projectVoucher.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UtilService() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public UtilService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String hashApiKey(String apiKey) {
        return bCryptPasswordEncoder.encode(apiKey);
    }

    public boolean checkApiKey(String apiKey, String hashedApiKey) {
        return bCryptPasswordEncoder.matches(apiKey, hashedApiKey);
    }
}
