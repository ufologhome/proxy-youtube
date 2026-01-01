package com.example.youtubeproxy;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;

import java.net.InetSocketAddress;
import java.net.Proxy;

import okhttp3.OkHttpClient;

public class PlayerActivity extends AppCompatActivity {

    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        PlayerView view = findViewById(R.id.playerView);

        // Настройка Proxy к Go серверу
        Proxy proxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress("192.168.0.150", 8881) // IP ПК с Go
        );

        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        OkHttpDataSource.Factory factory =
                new OkHttpDataSource.Factory(client);

        player = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(factory))
                .build();

        view.setPlayer(player);

        String url = getIntent().getStringExtra("video_url");
        player.setMediaItem(MediaItem.fromUri(Uri.parse(url)));
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
