package com.xiaoyao.sp.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class App {
    private static final String DEFAULT_SERVER_HOST = "localhost";
    private static final int DEFAULT_SERVER_PORT = 9999;
    private static final int DEFAULT_CLIENT_START = 0;
    private static volatile boolean STOP_FLAG = false;

    public static InetSocketAddress getLocalInetAddress() {
        return new InetSocketAddress(8000);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        start();
    }

    public static void start() throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT));
        socket.getOutputStream().write(DEFAULT_CLIENT_START);
        socket.getOutputStream().flush();

        InputStream remoteIn = socket.getInputStream();
        int stepSize = 10;

        while (!STOP_FLAG) {
            if (remoteIn.available() != DEFAULT_CLIENT_START) {
                doRequest(remoteIn, socket.getOutputStream());
            } else {
                Thread.sleep(stepSize);
            }
        }
    }

    public static void stop() {
        STOP_FLAG = true;
    }

    public static void doRequest(InputStream in, OutputStream out) throws IOException, InterruptedException {
        Socket local = new Socket();
        local.connect(getLocalInetAddress());
        InputStream localIn = local.getInputStream();
        OutputStream localOut = local.getOutputStream();

        byte[] datas = new byte[in.available()];
        in.read(datas);
        System.out.println("--------------------------------------------------------");
        System.out.println("请求：");
        System.out.println(new String(datas));
        System.out.println("--------------------------------------------------------");
        localOut.write(datas);
        localOut.flush();

        int stepSize = 10;
        int maxTimes = 1000;
        int currStep = 0;
        while (localIn.available() == 0 && currStep < maxTimes) {
            Thread.sleep(stepSize);
            currStep++;
        }
        byte[] data = new byte[localIn.available()];
        localIn.read(data);
        System.out.println("--------------------------------------------------------");
        System.out.println("响应：");
        System.out.println(new String(data));
        System.out.println("--------------------------------------------------------");
        if (data.length == 0) {
            out.write(-1);
        } else {
            out.write(data);
        }
        out.flush();
        local.close();

    }
}
