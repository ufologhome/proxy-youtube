package com.example.youtubeproxy;

import java.io.InputStream;
import java.io.OutputStream;

public class TunnelThread extends Thread {

    private final InputStream in;
    private final OutputStream out;

    public TunnelThread(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[32767];
        int len;

        try {
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
                out.flush();
            }
        } catch (Exception ignored) {}
    }
}
