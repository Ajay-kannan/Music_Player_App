package com.example.myapplicationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplicationdemo.recycleCard.ItemsList;
import com.example.myapplicationdemo.recycleCard.MyAdapterClassRecycleCard;
import com.example.myapplicationdemo.recycleSongs.ExoSongAdapter;
import com.example.myapplicationdemo.recycleSongs.GetSongs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SongsList extends AppCompatActivity  {
    RecyclerView recyclerView;
    List <GetSongs> mUpload;
    ExoSongAdapter exoSongAdapter;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    DatabaseReference databaseReferenceOther;
    ValueEventListener valueEventListenerOther;
    RecyclerView recyclerViewOther;
    List <ItemsList> list;
    MyAdapterClassRecycleCard myAdapterClassRecycleCard;
    TextView songTitle;
    ImageView sondCardImage;
    Button followBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);
        songTitle = findViewById(R.id.song_title);
        sondCardImage = findViewById(R.id.sond_card_image);
        recyclerView = findViewById(R.id.recycleViewSong);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUpload = new ArrayList<>();
        followBtn = findViewById(R.id.follow_btn);
        recyclerView.setAdapter(exoSongAdapter);
        exoSongAdapter = new ExoSongAdapter(getApplicationContext(),mUpload);
        databaseReference = FirebaseDatabase.getInstance().getReference("songs");

        recyclerViewOther = findViewById(R.id.recycle_view_song_list);
        recyclerViewOther.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL ,false) );
        list = new ArrayList<>();
        databaseReferenceOther = FirebaseDatabase.getInstance().getReference("uploads");

        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUpload.clear();
                for (DataSnapshot dss : snapshot.getChildren())
                {
                    GetSongs getSongs = dss.getValue(GetSongs.class);
                    getSongs.setmKey(dss.getKey());
                    final String s = getIntent().getExtras().getString("songTitle");
                    if (s.equals(getSongs.getArtistName()))
                    {
                        mUpload.add(getSongs);
                    }
                }
                recyclerView.setAdapter(exoSongAdapter);
                exoSongAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        String titleStr = getIntent().getExtras().getString("songTitle");
        songTitle.setText(titleStr);
        final String urlStr = getIntent().getExtras().getString("imageUrl");
        Glide.with(getApplicationContext()).load(urlStr).into(sondCardImage);

        valueEventListenerOther = databaseReferenceOther.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot itemSnapshot : snapshot.getChildren()){
                    ItemsList itemsList = itemSnapshot.getValue(ItemsList.class);
                    if (!itemsList.getName().equals(titleStr)) {
                        list.add(itemsList);
                    }
                }
                myAdapterClassRecycleCard = new MyAdapterClassRecycleCard(getApplicationContext(), list);
                recyclerViewOther.setAdapter(myAdapterClassRecycleCard);
                myAdapterClassRecycleCard.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
    }
}
