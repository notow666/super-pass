package com.xiaoyao.sp.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final Map<String, Socket> SOCKET_REPORSTORY = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("==============server start==============");
        ServerSocket serverSocket = new ServerSocket();
        Integer port = 9999;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = Integer.parseInt(System.getProperty("port", "9999"));
        }
        if (port == 9999) {
            System.err.println("未指定服务端口号，默认采用9999");
        }

        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("服务器已启动，开放端口9999");
        while (true) {
            Socket client = serverSocket.accept();
            System.out.printf("收到请求 from[%s]\n", client.getInetAddress());
            InputStream in = client.getInputStream();
            int prefix = 0;
            if ((prefix = in.read()) == 0) {
                doClient(client);
            } else {
                doRequest(client, prefix);
            }
        }
    }

    public static void doRequest(Socket requestSocket, int prefix) throws IOException, InterruptedException {
        Socket client = SOCKET_REPORSTORY.get("C");
        InputStream remoteIn = requestSocket.getInputStream();
        OutputStream remoteOut = requestSocket.getOutputStream();
        InputStream clientIn = client.getInputStream();
        OutputStream clientOut = client.getOutputStream();
        byte[] data = new byte[remoteIn.available()];
        remoteIn.read(data);
        System.out.println("--------------------------------------------------------");
        byte[] requestData = new byte[data.length + 1];
        requestData[0] = (byte) prefix;
        System.arraycopy(data, 0, requestData, 1, data.length);
        System.out.println("外部请求：");
        System.out.println(new String(requestData));
        System.out.println("--------------------------------------------------------");
        clientOut.write(requestData);

        int stepSize = 10;
        int maxTimes = 1000;
        int currTime = 0;
        while (clientIn.available() == 0 && currTime < maxTimes) {
            Thread.sleep(stepSize);
            currTime++;
        }

        data = new byte[clientIn.available()];
        clientIn.read(data);

        System.out.println("--------------------------------------------------------");
        System.out.println("客户端响应：");
        System.out.println(new String(data));
        System.out.println("--------------------------------------------------------");

        remoteOut.write(data);
        remoteOut.flush();

        requestSocket.close();
    }

    public static void doClient(Socket clientSocket) {
        SOCKET_REPORSTORY.put("C", clientSocket);
    }
}
