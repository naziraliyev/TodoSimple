package com.example.todosimple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText taskedit;
    private AdapterHelper adapterHelper;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseInstance;
    private Calendar calander;
    private SimpleDateFormat simpleDateFormat;
    private List<ToDoModel> toDoModelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskedit = findViewById(R.id.edit_task);
        recyclerView = findViewById(R.id.recyclerView_task);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        if (toDoModelList.size()>0){
        loadedData(taskedit.getText().toString());
        }else {
            Toast.makeText(this, "Can not find Any data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNotification() {

        // intent triggered, you can add other intent for other actions
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
        Uri urinotify = null;
        //Notification sound
        try {
            urinotify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), urinotify);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
        Notification mNotification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            mNotification = new Notification.Builder(this)

                    .setContentTitle("Wings-Traccar!")
                    .setContentText("You are punched-in for more than 10hrs!")
                    .setContentIntent(pIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(urinotify)
                    .build();

        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // If you want to hide the notification after it was selected, do the code below
        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, mNotification);
    }

    public void AddTask(View view) {

        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentTime = simpleDateFormat.format(calander.getTime());

        String task = taskedit.getText().toString();
        if (!task.equals("")) {
            writeDataToFirebase(task, currentTime);
            loadedData(task);
        } else {
            Toast.makeText(this, "Please write to task place", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadedData(String task) {
        Toast.makeText(this, "Data is loading ", Toast.LENGTH_LONG).show();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference().child("Tasks").child(task);
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        ToDoModel l=npsnapshot.getValue(ToDoModel.class);
                        toDoModelList.add(l);
                        Toast.makeText(MainActivity.this, "successfully loaded", Toast.LENGTH_SHORT).show();
                    }
                    adapterHelper=new AdapterHelper(MainActivity.this,toDoModelList);
                    recyclerView.setAdapter(adapterHelper);

                }else {
                    Toast.makeText(MainActivity.this, "Data is not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void writeDataToFirebase(String task, String currentTime) {

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child("Tasks").child(task).exists()) {
                  HashMap<String,String> myMap = new HashMap<>();
                  myMap.put("task",task);
                  myMap.put("time",currentTime);
                  myRef.child("Task").child(task).setValue(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          Toast.makeText(MainActivity.this, "Success "+task, Toast.LENGTH_SHORT).show();
                      }
                  });


                } else {
                    Toast.makeText(MainActivity.this, "Your data is already exist ...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
