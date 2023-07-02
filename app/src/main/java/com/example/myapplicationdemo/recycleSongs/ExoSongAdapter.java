package com.example.myapplicationdemo.recycleSongs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationdemo.MusicPlayerActivity;
import com.example.myapplicationdemo.R;

import java.io.Serializable;
import java.util.List;

public class ExoSongAdapter extends RecyclerView.Adapter<SongsAdapterViewHolder> {
    Context context;
    List<GetSongs> arrayListSongs;
    private int selectPosition;
    int num = 1;
    public ExoSongAdapter(Context context, List<GetSongs> arrayListSongs) {
        this.context = context;
        this.arrayListSongs = arrayListSongs;
        num =1;
    }

    @NonNull
    @Override
    public SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.song_card_resource,parent,false);
        return new SongsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapterViewHolder holder, int position) {
        GetSongs getSongs = arrayListSongs.get(position);
        int pos = position;

        holder.song_title.setText(getSongs.getSongTitle());
        holder.song_artist.setText(getSongs.getArtist());
        String duration = Utility.convertDuration(Long.parseLong(getSongs.getSongDuration()));
        holder.song_duration.setText(duration);
        holder.song_num.setText(num+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = pos;
                Intent intent = new Intent(context , MusicPlayerActivity.class);
                intent.putExtra("LIST", (Serializable) arrayListSongs);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        num += 1;
    }
    @Override
    public int getItemCount() {
        return arrayListSongs.size();
    }
}
