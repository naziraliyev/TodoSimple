package com.example.todosimple;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    private FirebaseDatabase firbaseDatabase;
    private DatabaseReference databaseReference;
    private List<ToDoModel> list1 = new ArrayList<>();

    public interface DataStatus {
        void DataIsloaded(List<ToDoModel> list, List<String> keys);

        void DataIsInserted();

        void DataIsUpdate();

        void DataIsDelete();
    }

    public void FirbaseDatabaseHelper() {
        firbaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firbaseDatabase.getReference("Tasks");
    }

    public void ReadData(final DataStatus dataStatus) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                   list1.clear();

                List<String> keys = new ArrayList<>();
                for (DataSnapshot keysNote : snapshot.getChildren()) {
                    keys.add(keysNote.getKey());
                    ToDoModel tasks = keysNote.getValue(ToDoModel.class);
                    list1.add(tasks);
                }
                dataStatus.DataIsloaded(list1, keys);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void AddTask(ToDoModel task, final DataStatus dataStatus) {

        String keys = databaseReference.push().getKey();
        databaseReference.child(keys).setValue(task).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });
    }

}
