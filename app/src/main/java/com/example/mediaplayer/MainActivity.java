package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button music , video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        music = findViewById(R.id.music_btn);
        video = findViewById(R.id.video_btn);

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TOmusicActivity = new Intent(MainActivity.this,MusicActivity.class);
                startActivity(TOmusicActivity);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent TOvideoActivity = new Intent(MainActivity.this,VideoActicity.class);
                startActivity(TOvideoActivity);
            }
        });
    }
}
