package com.example.my;

import android.app.ActivityGroup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.widget.TabHost;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {

    private TabHost tabHost;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, Intro.class);
        startActivity(intent);

            tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup(getLocalActivityManager());
            tabHost.addTab(tabHost.newTabSpec("home").setIndicator("홈").setContent(new Intent(MainActivity.this, Home.class)));
            tabHost.addTab(tabHost.newTabSpec("bus").setIndicator("버스").setContent(new Intent(MainActivity.this, Bus.class)));
            tabHost.addTab(tabHost.newTabSpec("map").setIndicator("지도").setContent(new Intent(MainActivity.this, Map.class)));
            tabHost.addTab(tabHost.newTabSpec("web").setIndicator("명지대").setContent(new Intent(MainActivity.this, Web.class)));
            tabHost.addTab(tabHost.newTabSpec("board").setIndicator("게시판").setContent(new Intent(MainActivity.this, Board.class)));
            tabHost.setCurrentTab(0);
            tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#A4A3A3"));

            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    if(tabId == "bus"){
                        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#A4A3A3"));
                        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(3).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(4).setBackgroundColor(Color.parseColor("#ffffff"));

                    }
                    else if(tabId == "map"){
                        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#A4A3A3"));
                        tabHost.getTabWidget().getChildAt(3).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(4).setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    else if(tabId == "web"){
                        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(3).setBackgroundColor(Color.parseColor("#A4A3A3"));
                        tabHost.getTabWidget().getChildAt(4).setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    else if(tabId == "board"){
                        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(3).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(4).setBackgroundColor(Color.parseColor("#A4A3A3"));
                    }
                    else if(tabId == "home"){
                        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#A4A3A3"));
                        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(3).setBackgroundColor(Color.parseColor("#ffffff"));
                        tabHost.getTabWidget().getChildAt(4).setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }
            });


    }

    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 꺼집니다.", Toast.LENGTH_SHORT).show();
        }

    }

}
