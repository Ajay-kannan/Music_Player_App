package com.example.myapplicationdemo;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationdemo.model.UploadSong;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class UploadSongs extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{
    TextView textViewImage;
    ProgressBar progressBar;
    Uri audioUri;
    StorageReference mStorageref;
    StorageTask mUploadsTask;
    DatabaseReference referenceSongs;
    String songCategory;
    MediaMetadataRetriever metadataRetriever;
    byte []art;
    String title1,artist1,album1,album_art1 ="" , durations1;
    TextView title,artist, durations,album,dataa;
    ImageView album_art ;
    Button uploadSongsbtn;
    String[] musicType = {"Classical","Theme Song","Love Song","Hip-Hop and Rap","Folk","Indie Song","sad Song"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_songs);

        textViewImage = findViewById(R.id.textViewSongFileSelected);
        progressBar = findViewById(R.id.progressBar);
        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        durations = findViewById(R.id.duration);
        album = findViewById(R.id.album);
        dataa = findViewById(R.id.dataa);
        album_art = findViewById(R.id.imageView);
        uploadSongsbtn = findViewById(R.id.openAudioFiles);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        metadataRetriever = new MediaMetadataRetriever();
        referenceSongs = database.getReference().child("songs");
        mStorageref = FirebaseStorage.getInstance().getReference().child("songs");

        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        List< String> categories = new ArrayList<>();

        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, musicType);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK && data.getData() != null)
        {
            audioUri = data.getData();
            String fileNames = getFileName(audioUri);
            textViewImage.setText(fileNames);
            metadataRetriever.setDataSource(getApplicationContext(),audioUri);

            art = metadataRetriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            album_art.setImageBitmap(bitmap);
            album.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            dataa.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            durations.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            title.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

            artist1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            durations1 = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        }

        else{
            Log.d("else block" ,"failed");
        }
    }

     public void openAudioFiles(View v){
         Intent i = new Intent();
         i.setType("audio/*");
         i.setAction(Intent.ACTION_OPEN_DOCUMENT);
         startActivityForResult(Intent.createChooser(i, "Select Audio"), 101);
     }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            songCategory = parent.getItemAtPosition(position).toString();
            Toast.makeText(this , "Selected : " + songCategory , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @SuppressLint("Range")
    private String getFileName(Uri uri)
    {
        String result = null;
        if(uri.getScheme().equals("content"))
        {
            Cursor cursor = getContentResolver().query(uri, null, null,null,null);
           try {
               if (cursor != null && cursor.moveToFirst())
               {
                   result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
               }
           }
           finally {
               cursor.close();
           }
        }

        if (result == null)
        {
            result = uri.getPath();
            int cut = result.lastIndexOf("/");
            if(cut != -1)
            {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void uploadFileTofirebase (View v)
    {
        if (textViewImage.equals("No file Selected"))
        {
            Toast.makeText(this, "please selected an image !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (mUploadsTask != null && mUploadsTask.isInProgress())
            {
                Toast.makeText(this, "songs uploads is already progress!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                uploadFiles();
            }
        }
    }

    private void uploadFiles()
    {
        if (audioUri != null)
        {
            Toast.makeText(this, "uploads please wait!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference storageReference = mStorageref.child(System.currentTimeMillis() + "."+getfileextension(audioUri));
            mUploadsTask = storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UploadSong uploadSong = new UploadSong(songCategory,title1,artist1,album_art1,durations1,uri.toString());
                            String uploadId = referenceSongs.push().getKey();
                            referenceSongs.child(uploadId).setValue(uploadSong);
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });

        }else{
            Toast.makeText(this, "no file selected to uploads", Toast.LENGTH_SHORT).show();
        }
    }

    private String getfileextension(Uri audioUri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));
    }

    public void openAlbumUploadsActivity(View v)
    {
        Intent i = new Intent(getApplicationContext(),UploadAlbumSongs.class);
        startActivity(i);
    }
}