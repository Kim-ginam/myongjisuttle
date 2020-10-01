package com.example.my;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class Bus extends AppCompatActivity {

    double mLatitude;
    double mLongitude;
    double mLatitude2;
    double mLongitude2;
    String api_key = "5729e968d46157f86f00961e030404a0c7f04cc7";
    //스파코사, 고객사 양사간 보안을 유지하고 외부에 노출되면 안되는키
    String server_key = "882306a8ed4b593832c0968a0c450a100dd0bd89";
    String member_key = "412406d254cd752eb081d6414e029ad8e0164a54";

    public static String getAPICertKey(long timestamp, String api_key, String server_key) {
        String hash_in = timestamp+"|"+api_key+"|"+server_key;
        String result = "";
        byte[] input = hash_in.getBytes();

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(input, 0, input.length);
            result = new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public void find_location() {
        long timestamp = new Date().getTime();
        long beforeTime = timestamp /1000 - 120;
        long todateTime = timestamp /1000 - 1;
        String cert_key = getAPICertKey(timestamp, api_key, server_key);

        String url = "http://cms.catchloc.com/api.get.member.location.list.php?api_key=" + api_key + "&member_key=" + member_key + "&timestamp=" + timestamp + "&cert_key=" + cert_key + "&from_date=" + beforeTime + "&to_date=" + todateTime;

        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        if (i == 0) {
                            JSONObject object = response.getJSONObject(i);
                            mLatitude = object.getDouble("latitude");
                            mLongitude = object.getDouble("longitude");
                        }
                        if (i == response.length() - 1) {
                            JSONObject object = response.getJSONObject(i);
                            mLatitude2 = object.getDouble("latitude");
                            mLongitude2 = object.getDouble("longitude");
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jor);
    }

    ArrayList<List> busStopList;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;
    ListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus);
        find_location();
        this.InitializeBusStopList();

        ListView listView = (ListView)findViewById(R.id.listView);
        myAdapter = new ListAdapter(this,busStopList);

        listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getName(),
                        Toast.LENGTH_LONG).show();
            }
        });
        Button timetable = (Button) findViewById(R.id.button2);
        final Button current = (Button) findViewById(R.id.button1);
        Button reset = (Button) findViewById(R.id.button);
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetButtonClicked();
            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Bus.this);
                dlg.setTitle("시간표");
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setView(R.layout.dialog);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Bus.this,"확인",Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });
    }

    private void onResetButtonClicked() {
        myAdapter.isClicked = false;
        myAdapter.notifyDataSetChanged();
    }

    private void onButtonClicked() {
        myAdapter.isClicked = !myAdapter.isClicked;
        find_location();
        myAdapter.notifyDataSetChanged();
    }

    public void InitializeBusStopList()
    {
        busStopList = new ArrayList<List>();
        // 첫 번째 아이템 추가.
        busStopList.add(new List(R.drawable.bus2,"버스관리사무소 정류장 (기점)","0분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        // 두 번째 아이템 추가.
        busStopList.add(new List(R.drawable.bus2, "상공회의소 버스 정류장","3분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        // 세 번째 아이템 추가.
        busStopList.add(new List(R.drawable.bus2, "진입로 정류장","6분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        busStopList.add(new List(R.drawable.bus2, "동부경찰서 중앙지구대 앞","12분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        busStopList.add(new List(R.drawable.bus2, "용인 CGV앞","15분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        busStopList.add(new List(R.drawable.bus2, "중앙공영주차장 앞","18분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        busStopList.add(new List(R.drawable.bus2, "진입로 정류장","21분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        busStopList.add(new List(R.drawable.bus2, "이마트 앞","24분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        busStopList.add(new List(R.drawable.bus2, "제1 공학관","27분"));
        busStopList.add(new List(R.drawable.bus2,"",""));
        busStopList.add(new List(R.drawable.bus2, "제3 공학관","30분"));

    }
    @Override
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
            Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 꺼집니다." + mLatitude , Toast.LENGTH_SHORT).show();
        }

    }

    public class ListAdapter extends BaseAdapter {

        Context mContext = null;
        LayoutInflater mLayoutInflater = null;
        ArrayList<List> busStop;

        public boolean isClicked = false;

        public ListAdapter(Context context, ArrayList<List> data) {
            mContext = context;
            busStop = data;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return busStop.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public List getItem(int position) {
            return busStop.get(position);
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            View view = mLayoutInflater.inflate(R.layout.listveiw, null);
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView time = (TextView)view.findViewById(R.id.time);

            if (isClicked) {
                if (mLongitude < 127.187598 && mLatitude > 37.2241405) {
                    if (position == 0) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude > 127.187599 && mLongitude <= 127.188248 && mLatitude > 37.2241406 && mLatitude <= 37.231328
                         && mLatitude > mLatitude2) {
                    if (position == 1) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude > 127.188248 && mLongitude <= 127.188623 && mLatitude > 37.231328 && mLatitude <= 37.233696
                        && mLatitude > mLatitude2) {
                    if (position == 3) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude > 127.188759 && mLongitude <= 127.198170 && mLongitude > mLongitude2) {
                    if (position == 5) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude > 127.198170 && mLongitude <= 127.206682) {
                    if (position == 7) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude > 127.206682 && mLongitude <= 127.212584 && mLatitude >= 37.2355804) {
                    if (position == 9) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude > 127.188675 && mLongitude <= 127.209054 && mLongitude < mLongitude2) {
                    if (position == 11) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude > 127.188211 && mLongitude <= 127.188675 && mLatitude < mLatitude2) {
                    if (position == 13) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
                else if (mLongitude <= 127.188211 && mLatitude < mLatitude2) {
                    if (position == 15) {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }
            imageView.setImageResource(busStop.get(position).getPrint());
            name.setText(busStop.get(position).getName());
            time.setText(busStop.get(position).getTime());

            return view;
        }
    }
}


