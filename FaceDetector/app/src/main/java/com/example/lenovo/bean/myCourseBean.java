package com.example.lenovo.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by lenovo on 2015/11/5.
 */

public class myCourseBean implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    public String title;
    public String provider;
    public Drawable photo;
    public String url;
    public int progress;

    public myCourseBean(String title, Drawable photo, String url, String provider,int progress) {
        this.title = title;
        this.photo = photo;
        this.url = url;
        this.provider = provider;
        this.progress = progress;
    }
}

