package com.example.youtubeproxy;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    private ExoPlayer p;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        PlayerView v = new PlayerView(this);
        setContentView(v);

        p = new ExoPlayer.Builder(this).build();
        v.setPlayer(p);

        // ЭТО СПЕЦИАЛЬНО — ЛЮБОЙ HTTPS
        p.setMediaItem(MediaItem.fromUri(
            Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
        ));
        p.prepare();
        p.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        p.release();
    }
}
