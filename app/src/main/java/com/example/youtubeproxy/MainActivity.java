package com.example.youtubeproxy;

import android.content.Intent;
import android.net.VpnService;
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

        // ===== 1. ЗАПРОС РАЗРЕШЕНИЯ НА VPN =====
        Intent vpnIntent = VpnService.prepare(this);
        if (vpnIntent != null) {
            // Первый запуск — Android покажет системный диалог
            startActivityForResult(vpnIntent, 0);
        } else {
            // Разрешение уже есть
            startService(new Intent(this, MyVpnService.class));
        }

        // ===== 2. СПИСОК ВИДЕО =====
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<VideoItem> videos = new ArrayList<>();
        videos.add(new VideoItem(
                "dQw4w9WgXcQ",
                "Never Gonna Give You Up",
                "https://img.youtube.com/vi/dQw4w9WgXcQ/0.jpg"
        ));
        videos.add(new VideoItem(
                "kxopViU98Xo",
                "Keyboard Cat",
                "https://img.youtube.com/vi/kxopViU98Xo/0.jpg"
        ));

        VideoAdapter adapter = new VideoAdapter(videos, video -> {
            Intent i = new Intent(this, PlayerActivity.class);
            i.putExtra("video_id", video.id);
            startActivity(i);
        });

        recyclerView.setAdapter(adapter);
    }

    // ===== 3. ОБРАБОТКА РАЗРЕШЕНИЯ VPN =====
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Пользователь нажал "Разрешить VPN"
            startService(new Intent(this, MyVpnService.class));
        }
    }
}
