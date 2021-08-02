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

import java.util.List;

public class AdapterHelper  {

   private Context mContext;
   private TaskAdapter taskAdapter;



    public void setConfig(RecyclerView recyclerView,Context context,List<ToDoModel> mToDoModelList,List<String>  mKeys){
       mContext =context;
       taskAdapter = new TaskAdapter(mToDoModelList,mKeys);
       recyclerView.setLayoutManager(new LinearLayoutManager(context));
       recyclerView.setAdapter(taskAdapter);
   }

    public class MyHolderView extends RecyclerView.ViewHolder {
        private TextView task,date;
        private String key;

        public MyHolderView(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
            date = itemView.findViewById(R.id.date);
        }
        public void bind(ToDoModel toDoModel, String key){
                  task.setText(toDoModel.getTask());
                  date.setText(toDoModel.getDate());
                  this.key = key;

        }
    }
   class TaskAdapter extends RecyclerView.Adapter<MyHolderView> {
        private List<ToDoModel> taskList;
        private List<String> keys;

       public TaskAdapter(List<ToDoModel> taskList, List<String> keys) {
           this.taskList = taskList;
           this.keys = keys;
       }

       @Override
        public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolderView(parent);
        }

        @Override
        public void onBindViewHolder( AdapterHelper.MyHolderView holder, int position) {
            holder.bind(taskList.get(position),keys.get(position));
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}
