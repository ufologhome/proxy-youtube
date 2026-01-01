package com.example.youtubeproxy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<VideoItem> videos = new ArrayList<>();
        // Добавляем одно видео BigBuckBunny
        videos.add(new VideoItem(
                "BigBuckBunny",
                "Big Buck Bunny",
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ));

        VideoAdapter adapter = new VideoAdapter(videos, video -> {
            Intent i = new Intent(this, PlayerActivity.class);
            i.putExtra("video_url", video.url);
            startActivity(i);
        });

        recyclerView.setAdapter(adapter);
    }
}
