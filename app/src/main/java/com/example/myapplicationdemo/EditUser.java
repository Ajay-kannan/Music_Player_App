package com.example.myapplicationdemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplicationdemo.model.DataClassEdit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditUser extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;
    EditText uploadName;
    String imageURL;
    String title;
    String email,key,oldImageURL,name;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        uploadImage = findViewById(R.id.uploadImage);
        uploadName = findViewById(R.id.uploadTopic);
        saveButton = findViewById(R.id.saveButton);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK)
                {
                    Intent data = result.getData();
                    uri = data.getData();
                    uploadImage.setImageURI(uri);
                }
                else {
                    Toast.makeText(EditUser.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        Log.d("bundle",bundle.toString());
        if (bundle != null){
            email = bundle.getString("email");
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
            name = bundle.getString("Name");
        }

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData()
    {
        storageReference = FirebaseStorage.getInstance().getReference().child("users").child(uri.getLastPathSegment());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (! uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                updateData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData() {
        title = uploadName.getText().toString().trim();
        DataClassEdit dataClassEdit = new DataClassEdit(title,imageURL,email);

        if(name == "null" || oldImageURL == "null")
        {
            Log.d("null part", "ok");
            FirebaseDatabase.getInstance().getReference("Users")
                    .setValue(dataClassEdit).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            return;
        }

        databaseReference.setValue(dataClassEdit).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                        StorageReference reference =
                                FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete();
                    Toast.makeText(EditUser.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUser.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}