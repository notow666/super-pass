package com.xiaoyao.sp.core.handler;

import com.xiaoyao.sp.core.commons.Request;

public interface Handler {
    default void preHandle(Request request) {
    }

    void doHandle(Request request);

    default void postHandle(Request request) {
    }
}
