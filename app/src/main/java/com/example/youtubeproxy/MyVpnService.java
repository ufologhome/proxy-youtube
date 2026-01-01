package com.example.youtubeproxy;

import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.content.Intent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class MyVpnService extends VpnService implements Runnable {

    private Thread thread;
    private ParcelFileDescriptor tun;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (thread == null) {
            thread = new Thread(this, "VPNThread");
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public void run() {
        try {
            Builder builder = new Builder();
            builder.setSession("YT-VPN");
            builder.addAddress("10.0.0.2", 32);
            builder.addRoute("0.0.0.0", 0); // ВСЁ через VPN
            builder.addDnsServer("8.8.8.8");

            tun = builder.establish();

            FileInputStream in = new FileInputStream(tun.getFileDescriptor());
            FileOutputStream out = new FileOutputStream(tun.getFileDescriptor());

            Socket socket = new Socket("192.168.0.150", 8881);

            TunnelThread tunToGo =
                    new TunnelThread(in, socket.getOutputStream());
            TunnelThread goToTun =
                    new TunnelThread(socket.getInputStream(), out);

            tunToGo.start();
            goToTun.start();

            tunToGo.join();
            goToTun.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
