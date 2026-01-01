package com.example.youtubeproxy;

import android.app.Service;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MyVpnService extends VpnService {

    private static final String TAG = "MyVpnService";
    private ParcelFileDescriptor vpnInterface;
    private Thread vpnThread;
    private volatile boolean running = false;

    private String GO_HOST = "192.168.0.150"; // ПК с Go сервером
    private int GO_PORT = 8881;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (vpnThread != null && running) return START_STICKY;

        running = true;
        vpnThread = new Thread(this::runVpn, "VPNThread");
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
                    try (Socket sock = new Socket()) {
                        sock.connect(new InetSocketAddress(GO_HOST, GO_PORT));
                        sock.getOutputStream().write(buffer, 0, length);

                        int n;
                        byte[] resp = new byte[32767];
                        while ((n = sock.getInputStream().read(resp)) > 0) {
                            out.write(resp, 0, n);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Go connect error: " + e);
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "VPN error", e);
        } finally {
            try {
                if (vpnInterface != null) vpnInterface.close();
            } catch (Exception ignored) {}
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
