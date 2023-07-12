package com.example.myapplicationdemo.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplicationdemo.EditUser;
import com.example.myapplicationdemo.Login;
import com.example.myapplicationdemo.R;
import com.example.myapplicationdemo.UploadAlbumSongs;
import com.example.myapplicationdemo.UploadSongs;
import com.example.myapplicationdemo.model.DataClassEdit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    CardView uploadBtn;
    CardView editBtn;
    CardView logOut;
    FirebaseAuth auth;

    FirebaseUser username;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ImageView userImage;
    TextView userName;
    DataClassEdit originalData = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        uploadBtn = view.findViewById(R.id.uploadBtn);
        editBtn = view.findViewById(R.id.about_edit);
        logOut = view.findViewById(R.id.log_out);
        auth = FirebaseAuth.getInstance();
        username = auth.getCurrentUser();
        Log.d("user", username.getEmail());
        userName = view.findViewById(R.id.user_name);
        userImage = view.findViewById(R.id.user_image);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapShot : snapshot.getChildren()){
                    DataClassEdit dataClassEdit = itemSnapShot.getValue(DataClassEdit.class);
                    if(username.getEmail().equals(dataClassEdit.getEmail()))
                    {
                            originalData = dataClassEdit;
                            originalData.setKey(itemSnapShot.getKey());
                    }
                }
                if(originalData == null)
                {
                    userName.setText("User Name update");
                    userImage.setImageResource(R.drawable.user_image);
                }
                else{
                    userName.setText(originalData.getDataName());
                    Glide.with(getContext()).load(originalData.getDataImage()).into(userImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), UploadAlbumSongs.class);
                startActivity(i);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditUser.class);
                if(originalData == null)
                {
                    intent.putExtra("email","null");
                    intent.putExtra("Image","null");
                    intent.putExtra("Name","null");
                    intent.putExtra("Key", "null");
                }
                else {
                    intent.putExtra("email",originalData.getEmail());
                    intent.putExtra("Image",originalData.getDataImage());
                    intent.putExtra("Name",originalData.getDataName());
                    intent.putExtra("Key",originalData.getKey());
                }
                getContext().startActivity(intent);
            }
        });
        return view;
    }
}