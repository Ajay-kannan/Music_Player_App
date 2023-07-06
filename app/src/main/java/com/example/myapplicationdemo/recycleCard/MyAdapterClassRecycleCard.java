package com.example.myapplicationdemo.recycleCard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplicationdemo.R;
import com.example.myapplicationdemo.SongsList;

import java.util.List;

public class MyAdapterClassRecycleCard extends RecyclerView.Adapter<CardItem> {

    Context context;
    List<ItemsList> list;

    public MyAdapterClassRecycleCard(Context context, List<ItemsList> list) {
        this.context = context;
        this.list = list;
        Log.d("checkName", String.valueOf(list));
    }

    @NonNull
    @Override
    public CardItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardItem(LayoutInflater.from(context).inflate(R.layout.musix_card_resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardItem holder, int position) {
           final ItemsList itemsList = list.get(position);
           holder.title.setText(itemsList.getName());
           Glide.with(context).load(itemsList.getUrl()).into(holder.img_src);

           holder.cardView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, SongsList.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   intent.putExtra("songsCategory", itemsList.getSongsCategory());
                   intent.putExtra("songTitle",itemsList.getName());
                   intent.putExtra("imageUrl",itemsList.getUrl());
                   context.startActivity(intent);
               }
           });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
