package com.example.myapplicationdemo.recycleCard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationdemo.R;

public class CardItem extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView img_src;
    public CardView cardView;
    public CardItem(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.recycle_name);
        img_src = (ImageView) itemView.findViewById(R.id.recycle_img);
        cardView = itemView.findViewById(R.id.card_view_id);
    }

}
