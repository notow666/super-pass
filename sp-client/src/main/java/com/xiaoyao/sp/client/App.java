package com.xiaoyao.sp.client;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class App {
    private static final int DEFAULT_CLIENT_CONNECTION_CONTENT = 0;
    private static volatile boolean STOP_FLAG = false;
    private static final String DEFAULT_CHAR_OF_KV_LINK = "=";

    private static final String DEFAULT_LOCAL_KEY = "local";
    private static final String DEFAULT_REMOTE_KEY = "remote";
    private static final Map<String, String> CONFIG = new ConcurrentHashMap<>();


    public static InetSocketAddress getLocalInetAddress() {
        String localAddress = CONFIG.get(DEFAULT_LOCAL_KEY);
        String[] localHP = localAddress.split(":");
        if (2 != localHP.length) {
            System.out.println("local参数非法！");
            System.exit(-1);
        }
        return new InetSocketAddress(localHP[0], Integer.parseInt(localHP[1]));
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String localAddress = System.getProperty(DEFAULT_LOCAL_KEY);
        String remoteAddress = System.getProperty(DEFAULT_REMOTE_KEY);
        if (args.length < 2 && (StringUtils.isBlank(localAddress) || StringUtils.isBlank(remoteAddress))) {
            System.out.println("启动失败！未指定启动参数！");
            System.out.println("指定方式：java -jar spc.jar local=http://localhost:8080 remote=http://xxx.xxx:xx");
            System.exit(-1);
        }
        for (String arg : args) {
            String[] kv = arg.split(DEFAULT_CHAR_OF_KV_LINK);
            if (kv.length == 2) {
                CONFIG.put(kv[0].trim(), kv[1].trim());
            }
        }

        System.out.println(CONFIG);

        start();
    }

    public static void start() throws IOException, InterruptedException {
        String remoteAddress = CONFIG.get(DEFAULT_REMOTE_KEY);
        String[] remoteHP = remoteAddress.split(":");
        if (2 != remoteHP.length) {
            System.out.println("local参数非法！");
            System.exit(-1);
        }
        start(new InetSocketAddress(remoteHP[0], Integer.parseInt(remoteHP[1])));
    }

    private static void start(InetSocketAddress server) throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(server);
        socket.getOutputStream().write(DEFAULT_CLIENT_CONNECTION_CONTENT);
        socket.getOutputStream().flush();

        InputStream remoteIn = socket.getInputStream();
        int stepSize = 10;

        while (!STOP_FLAG) {
            if (remoteIn.available() != DEFAULT_CLIENT_CONNECTION_CONTENT) {
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
