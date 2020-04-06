package com.example.iochat.Notifications;

public class NotificationConfigObject {
    private final int icon;
    private final String title;
    private final String text;

    public NotificationConfigObject(int icon, String title, String text) {
        this.icon = icon;
        this.title = title;
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
