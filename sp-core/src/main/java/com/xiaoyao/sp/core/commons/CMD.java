package com.xiaoyao.sp.core.commons;

public enum CMD {
    Connect(1, "创建连接"), DisConnect(-1, "断开连接"), Work(0, "处理业务");
    private int code;
    private String des;

    CMD(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static CMD valueOfCode(int code) {
        for (CMD cmd : values()) {
            if (cmd.code == code) {
                return cmd;
            }
        }
        throw new IllegalStateException();
    }
}
