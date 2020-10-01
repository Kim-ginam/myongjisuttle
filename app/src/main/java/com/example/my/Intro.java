package com.example.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.my.MainActivity;
import com.example.my.R;


public class Intro extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro);
        try{
            Thread.sleep(1000);
           startLoading();


        }
        catch (InterruptedException e){
            e.printStackTrace();
        }


    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        }, 2000);
    }
}
