package com.example.youtubeproxy;

import java.io.InputStream;
import java.io.OutputStream;

public class TunnelThread extends Thread {

    private final InputStream in;
    private final OutputStream out;

    public TunnelThread(InputStream i, OutputStream o) {
        in = i;
        out = o;
    }

    @Override
    public void run() {
        byte[] buf = new byte[32767];
        try {
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
