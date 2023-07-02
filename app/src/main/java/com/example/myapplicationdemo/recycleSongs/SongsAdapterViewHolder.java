package com.example.myapplicationdemo.recycleSongs;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationdemo.R;

public class SongsAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView song_title,song_artist,song_duration,song_num;

    public SongsAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        song_title = itemView.findViewById(R.id.recycle_song_title);
        song_artist = itemView.findViewById(R.id.recycle_song_artists);
        song_duration = itemView.findViewById(R.id.recycle_song_dur);
        song_num = itemView.findViewById(R.id.recycle_song_num);
    }



}
