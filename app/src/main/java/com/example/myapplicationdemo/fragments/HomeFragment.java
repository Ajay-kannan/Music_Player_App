package com.example.myapplicationdemo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplicationdemo.MyAdapterClass;
import com.example.myapplicationdemo.R;
import com.example.myapplicationdemo.recycleCard.ItemsList;
import com.example.myapplicationdemo.recycleCard.MyAdapterClassRecycleCard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    List <ItemsList> list;
    DatabaseReference mDatabase;

    ValueEventListener eventListener;
    ProgressDialog progressDialog;
    CardView cardView;
    MyAdapterClassRecycleCard myAdapterClassRecycleCard;
    public HomeFragment() {
    }

    public HomeFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cardView = view.findViewById(R.id.cardView);
        cardView.setBackgroundResource(R.drawable.music_band);
        recyclerView = view.findViewById(R.id.recycle_view_main);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        progressDialog = new ProgressDialog(getContext());
        list = new ArrayList<>();
        progressDialog.setMessage("please wait...");
        progressDialog.show();


        mDatabase = FirebaseDatabase.getInstance().getReference("uploads");

        eventListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot itemSnapshot : snapshot.getChildren()){
                    ItemsList itemsList = itemSnapshot.getValue(ItemsList.class);
                    list.add(itemsList);
                }
                myAdapterClassRecycleCard = new MyAdapterClassRecycleCard(getContext(), list);
                recyclerView.setAdapter(myAdapterClassRecycleCard);
                myAdapterClassRecycleCard.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressDialog.dismiss();
//
//                for(DataSnapshot postsnapshot : snapshot.getChildren())
//                {
//                    ItemsList itemsList = postsnapshot.getValue(ItemsList.class);
//                    list.add(itemsList);
//                }
//                myAdapterClassRecycleCard = new MyAdapterClassRecycleCard(getContext(), list);
//                recyclerView.setAdapter(myAdapterClassRecycleCard);
//                myAdapterClassRecycleCard.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                progressDialog.dismiss();
//            }
//        });


        return view;
    }
}