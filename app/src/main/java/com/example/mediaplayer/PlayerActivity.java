package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button next_btn , pre_btn , pause_btn;
    TextView song_lable;
    SeekBar seekBar;
    static MediaPlayer mediaPlayer;
    int position;
    String sName;
    ArrayList<File> mySongs;
    Thread updateseekBar;
    private long backPressTime;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        next_btn = findViewById(R.id.next);
        pre_btn = findViewById(R.id.pre);
        pause_btn = findViewById(R.id.pause);
        song_lable = findViewById(R.id.song_lable);
        seekBar = findViewById(R.id.seek_bar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekBar = new Thread(){

            @Override
            public void run() {

                int total = mediaPlayer.getDuration();
                int currentPosition = 0;

                while(currentPosition < total ){
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        sName = mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");
        song_lable.setText(songName);
        song_lable.setSelected(true);

        position = bundle.getInt("pos" , 0);
        Uri u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext() , u );
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        updateseekBar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary) , PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary) , PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                seekBar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    pause_btn.setBackgroundResource(R.drawable.play_color);
                    mediaPlayer.pause();
                }
                else{
                    pause_btn.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position+1)%mySongs.size();
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext() , u );
                if(mediaPlayer.isPlaying()){
                    pause_btn.setBackgroundResource(R.drawable.play_color);
                    mediaPlayer.pause();
                }
                else{
                    pause_btn.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
                sName = mySongs.get(position).getName().toString();
                song_lable.setText(sName);

                mediaPlayer.start();
            }
        });

        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position-1)<0) ?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext() , u );
                sName = mySongs.get(position).getName().toString();
                song_lable.setText(sName);
                if(mediaPlayer.isPlaying()){
                    pause_btn.setBackgroundResource(R.drawable.play_color);
                    mediaPlayer.pause();
                }
                else{
                    pause_btn.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }

                mediaPlayer.start();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(backPressTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            mediaPlayer.stop();
            return;
        }else {
            Toast.makeText(getBaseContext(), "Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();
    }
}
