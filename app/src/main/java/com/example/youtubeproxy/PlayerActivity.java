package com.example.youtubeproxy;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        PlayerView view = new PlayerView(this);
        setContentView(view);

        player = new ExoPlayer.Builder(this).build();
        view.setPlayer(player);

        // ⚠️ ПРОСТОЙ HTTP MP4 (НЕ HTTPS, НЕ YT)
        Uri uri = Uri.parse(
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        );

        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
