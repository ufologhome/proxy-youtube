package com.example.youtubeproxy;

import android.app.Service;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MyVpnService extends VpnService {

    private static final String TAG = "MyVpnService";
    private ParcelFileDescriptor vpnInterface = null;
    private Thread vpnThread = null;
    private volatile boolean running = false;

    private String GO_HOST = "192.168.0.150"; // ваш ПК с Go
    private int GO_PORT = 8881;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (vpnThread != null && running) return START_STICKY;

        running = true;
        vpnThread = new Thread(this::runVpn, "MyVpnThread");
        vpnThread.start();
        return START_STICKY;
    }

    private void runVpn() {
        try {
            Builder builder = new Builder();
            builder.setSession("YTProxyVPN")
                    .addAddress("10.0.0.2", 32)
                    .addRoute("0.0.0.0", 0)
                    .setMtu(1500);

            vpnInterface = builder.establish();
            if (vpnInterface == null) return;

            Log.i(TAG, "VPN established");

            FileInputStream in = new FileInputStream(vpnInterface.getFileDescriptor());
            FileOutputStream out = new FileOutputStream(vpnInterface.getFileDescriptor());

            byte[] buffer = new byte[32767];

            while (running) {
                int length = in.read(buffer);
                if (length > 0) {
                    // Пересылаем TCP трафик на Go сервер
                    try (Socket sock = new Socket()) {
                        sock.connect(new InetSocketAddress(GO_HOST, GO_PORT));
                        sock.getOutputStream().write(buffer, 0, length);

                        // Читаем ответ и возвращаем в TUN
                        int n;
                        byte[] resp = new byte[32767];
                        while ((n = sock.getInputStream().read(resp)) > 0) {
                            out.write(resp, 0, n);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Go connect error: " + e);
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "VPN error: " + e);
        } finally {
            try {
                if (vpnInterface != null) vpnInterface.close();
            } catch (IOException ignored) {}
            running = false;
            Log.i(TAG, "VPN stopped");
        }
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }
}
