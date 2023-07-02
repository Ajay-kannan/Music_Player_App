package com.example.myapplicationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplicationdemo.notification.CreateNotification;
import com.example.myapplicationdemo.notification.GetNotificationData;
import com.example.myapplicationdemo.notification.services.OnClearFromRecentService;
import com.example.myapplicationdemo.recycleSongs.GetSongs;
import com.example.myapplicationdemo.recycleSongs.MyMediaPlayer;
import com.example.myapplicationdemo.recycleSongs.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {
    TextView title, currentTime,totalTime;
    SeekBar seekBar;
    ImageView pausePlay, nextBtn, previousBtn ;
    // Imageview musicIcon;
    ArrayList<GetSongs> songsList;
    GetSongs currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    NotificationManager notificationManager;
    GetNotificationData getNotificationData;
//    int x = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        title = findViewById(R.id.song_title_main);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        pausePlay = findViewById(R.id.pause_play);
        nextBtn = findViewById(R.id.next);
        previousBtn = findViewById(R.id.previous);
        title.setSelected(true);
        songsList =(ArrayList<GetSongs>) getIntent().getSerializableExtra("LIST");
        setResourcesMusic();
        getNotificationData = new GetNotificationData("a","b", 2);
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(Utility.convertDuration(mediaPlayer.getCurrentPosition()));
                    if (mediaPlayer.isPlaying())
                    {
                        pausePlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                        // musicIcon.setRotation(x++);
                    }
                    else{
                        pausePlay.setImageResource(R.drawable.baseline_play_circle_outline_24);
                        // musicIcon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser)
                {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }
        CreateNotification.createNotification(MusicPlayerActivity.this,getNotificationData,R.drawable.baseline_pause_24,MyMediaPlayer.currentIndex,songsList.size()-1);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Music App", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null)
            {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");
            Log.d("action", "came");
            switch (action){

                case CreateNotification.ACTION_PREVIOUS:
                    playPreviousSong();
                    break;
                case CreateNotification.ACTION_PLAY:

                    if(mediaPlayer.isPlaying()){
                        CreateNotification.createNotification(MusicPlayerActivity.this,getNotificationData,R.drawable.baseline_pause_24,MyMediaPlayer.currentIndex,songsList.size()-1);
                        mediaPlayer.pause();
                    }
                    else{
                        CreateNotification.createNotification(MusicPlayerActivity.this,getNotificationData,R.drawable.baseline_play_arrow_24,MyMediaPlayer.currentIndex,songsList.size()-1);
                        mediaPlayer.start();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    playNextSong();
                    break;
            }
        }
    };

    private void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getSongLink());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void playNextSong(){
        if (MyMediaPlayer.currentIndex == songsList.size()-1)
            return;

        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesMusic();
        CreateNotification.createNotification(MusicPlayerActivity.this,getNotificationData,R.drawable.baseline_pause_24,MyMediaPlayer.currentIndex,songsList.size()-1);
    }
    private void playPreviousSong()
    {
        if (MyMediaPlayer.currentIndex == 0)
            return;

        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesMusic();
        CreateNotification.createNotification(MusicPlayerActivity.this,getNotificationData,R.drawable.baseline_pause_24,MyMediaPlayer.currentIndex,songsList.size()-1);
    }
    private void pausePlay()
    {
        if(mediaPlayer.isPlaying()){
            CreateNotification.createNotification(MusicPlayerActivity.this,getNotificationData,R.drawable.baseline_pause_24,MyMediaPlayer.currentIndex,songsList.size()-1);
            mediaPlayer.pause();
        }
        else{
            CreateNotification.createNotification(MusicPlayerActivity.this,getNotificationData,R.drawable.baseline_play_arrow_24,MyMediaPlayer.currentIndex,songsList.size()-1);
            mediaPlayer.start();
        }
    }

    void setResourcesMusic()
    {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        title.setText(currentSong.getSongTitle());

        totalTime.setText(Utility.convertDuration(Long.parseLong(currentSong.getSongDuration())));
        pausePlay.setOnClickListener( v -> pausePlay());
        nextBtn.setOnClickListener( v -> playNextSong());
        previousBtn.setOnClickListener( v -> playPreviousSong());
        playMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }

    //    public static String covertToMMSS(String duration)
//    {
//        Long mills = Long.parseLong(duration);
//        return   String.format("%d:%02d",
//                TimeUnit.MILLISECONDS.toMinutes(mills) % TimeUnit.HOURS.toMinutes(1),
//                TimeUnit.MILLISECONDS.toSeconds(mills) % TimeUnit.HOURS.toHours(1));
//    }
}

