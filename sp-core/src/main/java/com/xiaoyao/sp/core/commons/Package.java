package com.xiaoyao.sp.core.commons;


public final class Package {
    private CMD cmd;
    private String token;

    private byte[] data;

    public CMD getCmd() {
        return cmd;
    }

    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
