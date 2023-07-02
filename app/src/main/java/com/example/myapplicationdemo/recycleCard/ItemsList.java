package com.example.myapplicationdemo.recycleCard;

public class ItemsList {
    String name;
    String url;
    String songsCategory;

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }

    public ItemsList(String name, String url) {
        this.name = name;
        this.url = url;
    }


    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public ItemsList() {
    }


}
