package com.example.youtubeproxy;

import android.net.VpnService;
import android.content.Intent;
import android.os.ParcelFileDescriptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MyVpnService extends VpnService {

    private Thread tunnel;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Builder builder = new Builder();
        builder.setSession("YT-VPN");
        builder.addAddress("10.0.0.2", 32);
        builder.addRoute("0.0.0.0", 0);
        builder.addDnsServer("8.8.8.8");

        ParcelFileDescriptor tun = builder.establish();

        tunnel = new TunnelThread(
                new FileInputStream(tun.getFileDescriptor()),
                new FileOutputStream(tun.getFileDescriptor())
        );
        tunnel.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (tunnel != null) tunnel.interrupt();
        super.onDestroy();
    }
}
