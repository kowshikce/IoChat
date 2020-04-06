package com.example.iochat.MessageAdapter;

public class message {
    private String pro;
    private String ms;

    public message(String pro, String ms) {
        this.pro = pro;
        this.ms = ms;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }
}
