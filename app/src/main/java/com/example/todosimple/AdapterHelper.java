package com.example.todosimple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterHelper extends RecyclerView.Adapter<AdapterHelper.MyHolderview>  {
    private Context context;
    private  List<ToDoModel> list = new ArrayList<>();


    public AdapterHelper(Context context, List<ToDoModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolderview onCreateViewHolder( ViewGroup parent, int viewType) {

        return new MyHolderview(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder( MyHolderview holder, int position) {

        ToDoModel item = list.get(position);
        holder.task.setText(item.task);
        holder.date.setText(item.date);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolderview extends RecyclerView.ViewHolder {
        TextView task,date;
        public MyHolderview( View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
            date = itemView.findViewById(R.id.date);

        }
    }
}
