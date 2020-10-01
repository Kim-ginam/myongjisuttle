package com.example.my;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Board extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Data> fires;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        recyclerView = findViewById(R.id.recyclerview); //아이디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fires = new ArrayList<>(); // Data객체를 담을 어레이리스트 (어댑터쪽으로)

        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Message");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스 데이터베이스에 데이터를 받아오는 곳
                fires.clear(); // 기존 배열 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ // 반복문으로 데이터 리스트 출력
                    Data data = snapshot.getValue(Data.class); //만들어뒀던 Data 객체에 데이터를 답는다.
                    fires.add(data);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던중 에러 발생 시
                Log.e("Borad",String.valueOf(databaseError.toException())); //에러문 출력

            }
        });

        adapter = new BoardAdapter(fires, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터연결


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

