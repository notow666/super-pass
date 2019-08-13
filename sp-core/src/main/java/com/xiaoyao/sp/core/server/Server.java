package com.xiaoyao.sp.core.server;

public interface Server {
    void start();

    default void start(Listener listener) {
        try {
            start();
            listener.success();
        } catch (Exception e) {
            listener.fail(e.getCause());
        }
    }

    void stop();

    default void stop(Listener listener) {
        try {
            stop();
            listener.success();
        } catch (Exception e) {
            listener.fail(e.getCause());
        }
    }
}
