package com.example.myapplicationdemo.model;

public class DataClassEdit {
    String dataName;
    String key;
    String dataImage;

    String email;

    public DataClassEdit(String dataName, String dataImage, String email) {
        this.dataName = dataName;
        this.dataImage = dataImage;
        this.email = email;
    }

    public DataClassEdit() {
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }
}
