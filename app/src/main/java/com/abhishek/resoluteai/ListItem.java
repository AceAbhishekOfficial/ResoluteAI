package com.abhishek.resoluteai;

public class ListItem {
    private String date;
    private String time;
    private String message;

    public ListItem(String date, String time, String message) {
        this.date = date;
        this.time = time;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}

