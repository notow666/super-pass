package com.xiaoyao.sp.core.event;

public enum EventType {
    Initialized(1), Readable(2), Waiting(4), Writable(8), Closable(16);

    private int code;

    public int getCode() {
        return code;
    }

    EventType(int code) {
        this.code = code;
    }

    public static EventType valueOfType(int code) {
        for (EventType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalStateException();
    }
}
