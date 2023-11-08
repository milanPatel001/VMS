package com.project.projectVoucher.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UseCaseBody {
    String discount;
    String extra;
    String batchNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime expiryDate;

    String pattern;
    String err;

    public UseCaseBody(){

    }

    public UseCaseBody(String err) {
        this.err = err;
    }

    public UseCaseBody(String discount, String extra, String batchNum, LocalDateTime expiryDate) {
        this.discount = discount;
        this.extra = extra;
        this.batchNum = batchNum;
        this.expiryDate = expiryDate;
    }

    public UseCaseBody(String discount, String extra, String batchNum, LocalDateTime expiryDate, String pattern) {
        this.discount = discount;
        this.extra = extra;
        this.batchNum = batchNum;
        this.expiryDate = expiryDate;
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
