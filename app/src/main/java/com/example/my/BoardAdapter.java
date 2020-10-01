package com.example.my;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CustomViewHolder> {

    private ArrayList<Data> fires;
    private Context context;


    public BoardAdapter(ArrayList<Data> fires, Context context) {
        this.fires = fires;
        this.context = context;
    }

    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView, textView2;

        public CustomViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.text1);
            textView2 = view.findViewById(R.id.text2);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textView.setText(fires.get(position).getTitle());
        holder.textView2.setText(fires.get(position).getContent());
    }


    @Override
    public int getItemCount() {
        return (fires != null ? fires.size() : 0);
    }


}