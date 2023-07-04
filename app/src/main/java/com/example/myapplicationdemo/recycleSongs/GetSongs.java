package com.example.myapplicationdemo.recycleSongs;

import androidx.annotation.NonNull;

import com.example.myapplicationdemo.R;

import java.io.Serializable;

public class GetSongs implements Serializable {
    public String songsCategory, songTitle, artist, album_art, songDuration , songLink, mKey,artistName;
    public int image;
    public GetSongs(String songTitle,String songDuration, String songLink) {
        this.songTitle = songTitle;
        this.songDuration = songDuration;
        this.songLink = songLink;
    }

    public GetSongs(String songTitle,String artist, int image) {
        this.songTitle = songTitle;
        this.artist = artist;
        this.image = image;
    }

    public GetSongs(String songsCategory, @NonNull String songTitle, String artist, String album_art, String songDuration, String songLink, String artistName) {
        if (songTitle.trim().equals(""))
        {
            songTitle = "No Title";
        }
        this.songsCategory = songsCategory;
        this.songTitle = songTitle;
        this.artist = artist;
        this.album_art = album_art;
        this.songDuration = songDuration;
        this.songLink = songLink;
        this.artistName = artistName;
    }

    public int getImage() {
        return R.drawable.music_band;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public GetSongs() {
    }

    public String getSongsCategory() {
        return songsCategory;
    }

    public void setSongsCategory(String songsCategory) {
        this.songsCategory = songsCategory;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
