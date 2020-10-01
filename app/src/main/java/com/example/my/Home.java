package com.example.my;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    TextView t1_temp, t2_description, t3_date, t4_youngin, t5_weather;
    ImageButton btn_facebook, btn_youtube, btn_insta, btn_blog;
    ViewFlipper viewFlipper;
    LinearLayout linearLayout;
    int id;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        t1_temp = (TextView) findViewById(R.id.temp);
        t2_description = (TextView) findViewById(R.id.weather_current);
        t3_date = (TextView) findViewById(R.id.date);
        t4_youngin = (TextView) findViewById(R.id.yongin);
        t5_weather = (TextView) findViewById(R.id.weather);
        btn_facebook = (ImageButton) findViewById(R.id.facebook);
        btn_youtube = (ImageButton) findViewById(R.id.youtube);
        btn_insta = (ImageButton) findViewById(R.id.insta);
        btn_blog = (ImageButton) findViewById(R.id.blog);
        linearLayout = (LinearLayout) findViewById(R.id.layout_current);

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/myongjiuniversity"));
                startActivity(intent);
            }
        });
        btn_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/channel/UCYuO1dJiQ_MSCrOEO8UmzDA"));
                startActivity(intent);
            }
        });
        btn_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/myongji_univ/"));
                startActivity(intent);
            }
        });
        btn_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://blog.naver.com/mjupr"));
                startActivity(intent);
            }
        });
        find_weather();
        int images[] = {R.drawable.m,R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5};
        viewFlipper = (ViewFlipper) findViewById(R.id.image_slide);
        for(int image : images){
            fllipperImages(image);
        }
    }

    public void find_weather() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=YONGIN&appid=4abd5593629c3f70fbbdda0a16c84a92&units=metric";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    id = object.getInt("id");
                    if(id == 800){
                        description = "맑음";
                    }
                    else if(id >= 200 && id <= 232){
                        description = "뇌우";
                    }
                    else if(id >= 300 && id <= 321){
                        description = "안개";
                    }
                    else if(id >= 500 && id <= 531){
                        description = "비";
                    }
                    else if(id >= 600 && id <= 622){
                        description = "눈";
                    }
                    else if(id >= 700 && id <= 781){
                        description = "안개";
                    }
                    else if(id >= 801 && id <= 804){
                        description = "흐림";
                    }

                   /* if(description .equals("clear sky")){
                        description = "맑음";
                    }
                    else if(description .equals("haze") ){
                        description = "안개";
                    }
                    else if(description .equals("fog") ){
                        description = "안개";
                    }
                    else if(description .contains("clouds") ){
                        description = "흐림";
                    }
                    else if(description .contains("rain") ){
                        description = "비";
                    }
                    else if(description .contains("drizzle") ){
                        description = "비";
                    }
                    else if(description .contains("snow") ){
                        description = "눈";
                    }*/
                    double temp_int = Double.parseDouble(temp);
                    double centi = temp_int;
                    centi = Math.floor(centi);
                    int i = (int) centi;
                    t1_temp.setText(String.valueOf(i)+"°C");
                    t2_description.setText(description);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 EE요일");
                    String formatted_date = sdf.format(calendar.getTime());
                    t3_date.setText(formatted_date);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH");
                    String formatted_date2 = sdf2.format(calendar.getTime());
                    if(Integer.parseInt(formatted_date2)>=18 || Integer.parseInt(formatted_date2)<=6){
                        linearLayout.setBackgroundResource(R.drawable.background11);
                        t1_temp.setTextColor(Color.WHITE);
                        t2_description.setTextColor(Color.WHITE);
                        t3_date.setTextColor(Color.WHITE);
                        t4_youngin.setTextColor(Color.WHITE);
                        t5_weather.setTextColor(Color.WHITE);
                    }
                    else{
                        linearLayout.setBackgroundResource(R.drawable.background9);
                        t1_temp.setTextColor(Color.BLACK);
                        t2_description.setTextColor(Color.BLACK);
                        t3_date.setTextColor(Color.BLACK);
                        t4_youngin.setTextColor(Color.BLACK);
                        t5_weather.setTextColor(Color.BLACK);

                    }


                   // double temp_int = Double.parseDouble(temp);
                   // double centi = (temp_int - 273.15);
                    //centi = Math.round(centi);
                   // int i = (int) centi;
                    //t1_temp.setText(String.valueOf(i)+"°C");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

    }
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);      // 이미지 추가
        viewFlipper.setFlipInterval(3000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        viewFlipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
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



