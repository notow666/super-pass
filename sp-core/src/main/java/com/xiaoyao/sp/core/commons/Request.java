package com.xiaoyao.sp.core.commons;

import java.net.Socket;

public final class Request {
    private final Socket from;
    private final Socket to;
    private final Package body;

    public Request(Socket from, Socket to, Package body) {
        this.from = from;
        this.to = to;
        this.body = body;
    }

    public Socket getFrom() {
        return from;
    }

    public Socket getTo() {
        return to;
    }

    public Package getBody() {
        return body;
    }
}
