package com.example.myapplicationdemo.notification;

import com.example.myapplicationdemo.R;

public class GetNotificationData {
    String songTitle, artist;
    int image;

    public GetNotificationData(String songTitle, String artist, int image) {
        this.songTitle = songTitle;
        this.artist = artist;
        this.image = image;
    }

    public String getSongTitle() {
        return "title 1";
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return "Artist";
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getImage() {
        return R.drawable.music_band;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
