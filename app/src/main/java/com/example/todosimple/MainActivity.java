package com.example.todosimple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todosimple.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText taskedit;
    List<ToDoModel> toDoModelList;
    List<String> list;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskedit = findViewById(R.id.edit_task);
        recyclerView = findViewById(R.id.recyclerView_task);
        toDoModelList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tasks");

//        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    private void insertedToFirebase() {
        ToDoModel task = new ToDoModel();
        task.setTask(taskedit.getText().toString());
        new FirebaseHelper().AddTask(task, new FirebaseHelper.DataStatus() {
            @Override
            public void DataIsloaded(List<ToDoModel> list, List<String> keys) {

            }

            @Override
            public void DataIsInserted() {
                Toast.makeText(MainActivity.this, "Data is add to firebase ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void DataIsUpdate() {

            }

            @Override
            public void DataIsDelete() {

            }
        });
    }

    private void loadedFromFirebase() {
        new FirebaseHelper().ReadData(new FirebaseHelper.DataStatus() {
            @Override
            public void DataIsloaded(List<ToDoModel> list, List<String> keys) {
                new AdapterHelper().setConfig(recyclerView,MainActivity.this,list,keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdate() {

            }

            @Override
            public void DataIsDelete() {

            }
        });
    }

    public void AddTask(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Tasks");
        String ts = taskedit.getText().toString();
        myRef.setValue(ts);
        readData();
       // AdapterHelper adapterHelper = new AdapterHelper.TaskAdapter(toDoModelList,list);
//        insertedToFirebase();
//        loadedFromFirebase();
    }

    private void readData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ToDoModel post = dataSnapshot.getValue(ToDoModel.class);
                toDoModelList.add(post);
                Toast.makeText(MainActivity.this, "Data is loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(postListener);
    }
}