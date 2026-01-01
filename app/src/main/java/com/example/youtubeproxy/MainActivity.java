package com.example.youtubeproxy;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        Intent vpn = VpnService.prepare(this);
        if (vpn != null) {
            startActivityForResult(vpn, 0);
        } else {
            startService(new Intent(this, MyVpnService.class));
            startActivity(new Intent(this, PlayerActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int r, int c, Intent d) {
        super.onActivityResult(r, c, d);
        startService(new Intent(this, MyVpnService.class));
        startActivity(new Intent(this, PlayerActivity.class));
    }
}
