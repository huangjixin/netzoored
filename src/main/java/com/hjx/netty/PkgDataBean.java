package com.hjx.netty;

public class PkgDataBean {
    //命令字
    private byte cmd;
    //数据长度
    private byte dataLength;
    //数据
    private String data;

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte getDataLength() {
        return dataLength;
    }

    public void setDataLength(byte dataLength) {
        this.dataLength = dataLength;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    //省略get/set函数
}