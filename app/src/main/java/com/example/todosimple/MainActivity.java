package com.example.todosimple;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText taskedit;
    private AdapterHelper adapterHelper;
    private List<ToDoModel> list = new ArrayList<>();
    DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskedit = findViewById(R.id.edit_task);
        recyclerView = findViewById(R.id.recyclerView_task);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        getPrefs();
//        if (list.size() != 0) {
//            loadedData();
//        } else {
//            Toast.makeText(this, "Can not find Any data", Toast.LENGTH_SHORT).show();
//        }

    }


    private void getPrefs() {
        SharedPreferences dateprefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        dateprefs.contains("date");

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
        String task = taskedit.getText().toString();
//        Date currentTime = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));

        String localTime = date.format(currentLocalTime);
        ToDoModel model = new ToDoModel(task,localTime.toString());
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
         mDbRef = mDatabase.getReference("Task/Name");
        mDbRef.setValue(model).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", e.getLocalizedMessage());
            }
        });
loadedData();

    }

    private void loadedData() {
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ToDoModel model = dataSnapshot.getValue(ToDoModel.class);
                list.add(model);
                adapterHelper = new AdapterHelper(MainActivity.this,list);
                recyclerView.setAdapter(adapterHelper);
                Log.d("TAG", "Value is: " + model);
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }


    private void setprefs(String dateSharedPrefs) {
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("date", dateSharedPrefs);
        editor.apply();
    }


}
