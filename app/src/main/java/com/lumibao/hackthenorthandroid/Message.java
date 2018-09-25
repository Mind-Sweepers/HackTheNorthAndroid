package com.lumibao.hackthenorthandroid;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by micha on 2018-09-23.
 */

public class Message {

    String language;
    String time;
    String message;

    public Message(String language, String time, String message) {
        this.language = language;
        this.time = time;
        this.message = message;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
