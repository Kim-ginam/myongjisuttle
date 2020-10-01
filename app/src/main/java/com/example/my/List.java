package com.example.my;

import android.graphics.drawable.Drawable;

public class List {
    private int print;
    private String name;
    private String time;

    public List(int print, String name, String time){
        this.print = print;
        this.name = name;
        this.time = time;
    }

    public int getPrint()
    {
        return this.print;
    }

    public String getName()
    {
        return this.name;
    }

    public String getTime()
    {
        return this.time;
    }
}