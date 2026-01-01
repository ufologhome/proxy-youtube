package com.example.youtubeproxy;

import java.io.*;
import java.net.Socket;

public class TunnelThread extends Thread {

    private final InputStream tunIn;
    private final OutputStream tunOut;

    public TunnelThread(InputStream in, OutputStream out) {
        this.tunIn = in;
        this.tunOut = out;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("192.168.0.150", 8881);
            InputStream goIn = socket.getInputStream();
            OutputStream goOut = socket.getOutputStream();

            // TUN → GO
            new Thread(() -> pipe(tunIn, goOut)).start();

            // GO → TUN
            pipe(goIn, tunOut);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pipe(InputStream in, OutputStream out) {
        byte[] buf = new byte[32767];
        int len;
        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
                out.flush();
            }
        } catch (IOException ignored) {}
    }
}
