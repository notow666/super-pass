package com.xiaoyao.sp.core.server;

public interface Listener {
    void success(Object... args);

    void fail(Throwable cause);
}
