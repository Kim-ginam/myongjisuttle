package com.example.my;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.Manifest;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    MapFragment mapFrag;

    public RequestQueue queue;
    double mLatitude;
    double mLongitude;

    String api_key = "5729e968d46157f86f00961e030404a0c7f04cc7";
    //스파코사, 고객사 양사간 보안을 유지하고 외부에 노출되면 안되는키
    String server_key = "882306a8ed4b593832c0968a0c450a100dd0bd89";
    String member_key = "412406d254cd752eb081d6414e029ad8e0164a54";
    long timestamp = new Date().getTime();
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;

    String cert_key = getAPICertKey(timestamp, api_key, server_key);

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
        String url = "http://cms.catchloc.com/api.get.member.location.last.php?api_key="+api_key+"&member_key="+member_key+"&timestamp="+timestamp+"&cert_key="+cert_key;

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mLatitude = response.getDouble("latitude");
                    mLongitude = response.getDouble("longitude");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        find_location();
        setContentView(R.layout.map);
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        Log.d("onCreatelatitude",String.valueOf(mLatitude));
        Button restart = (Button)findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap map) {

        gMap = map;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 15));
        Log.d("latitude12345",String.valueOf(mLatitude));
        MarkerOptions marker = new MarkerOptions();
        marker .position(new LatLng(mLatitude, mLongitude))
                .title("현재 위치")
                .snippet("Current Location");
        map.addMarker(marker).showInfoWindow();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 마커 클릭시 호출되는 콜백 메서드
                Toast.makeText(getApplicationContext(),
                        marker.getTitle() + " 위도" + mLatitude +"경도" + mLongitude
                        , Toast.LENGTH_SHORT).show();
                return false;

            }
        });
    }
    public void onResume(){
        super.onResume();
        find_location();
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
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



