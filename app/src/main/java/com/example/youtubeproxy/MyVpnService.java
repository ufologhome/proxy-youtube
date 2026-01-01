package com.example.youtubeproxy;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MyVpnService extends VpnService implements Runnable {

    private Thread thread;
    private ParcelFileDescriptor tun;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (thread == null) {
            thread = new Thread(this, "VPN-Thread");
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public void run() {
        try {
            Builder builder = new Builder();
            builder.setSession("YT-VPN");
            builder.setMtu(1500);
            builder.addAddress("10.0.0.2", 32);
            builder.addRoute("0.0.0.0", 0);
            builder.addDnsServer("8.8.8.8");

            tun = builder.establish();

            FileInputStream tunIn =
                    new FileInputStream(tun.getFileDescriptor());
            FileOutputStream tunOut =
                    new FileOutputStream(tun.getFileDescriptor());

            Socket socket = new Socket();
            protect(socket); // КРИТИЧЕСКИ ВАЖНО
            socket.connect(new InetSocketAddress("192.168.0.150", 8881));

            new TunnelThread(tunIn, socket.getOutputStream()).start();
            new TunnelThread(socket.getInputStream(), tunOut).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
