package com.example.youtubeproxy;

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
    public int onStartCommand(Intent i, int f, int id) {
        if (thread == null) {
            thread = new Thread(this, "VPN");
            thread.start();
        }
        return START_STICKY;
    }

    @Override
    public void run() {
        try {
            Builder b = new Builder();
            b.setSession("YT-VPN");
            b.setMtu(1500);
            b.addAddress("10.0.0.2", 32);
            b.addRoute("0.0.0.0", 0);
            b.addDnsServer("8.8.8.8");

            tun = b.establish();

            FileInputStream in = new FileInputStream(tun.getFileDescriptor());
            FileOutputStream out = new FileOutputStream(tun.getFileDescriptor());

            Socket s = new Socket();
            protect(s); // КРИТИЧЕСКИ ВАЖНО
            s.connect(new InetSocketAddress("192.168.0.150", 8881));

            new TunnelThread(in, s.getOutputStream()).start();
            new TunnelThread(s.getInputStream(), out).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
