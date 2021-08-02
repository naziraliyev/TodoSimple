package com.example.todosimple;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "myToDoList";
    private static final String TABLE_NAME = "myToDoListTable";


    public DbHelper( Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, task VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long WriteTable(String task) {
        ContentValues result = new ContentValues();
        result.put("task",task);
        SQLiteDatabase data = getWritableDatabase();
        long nat = data.insert( TABLE_NAME, null, result);
        return nat;
    }
    public Cursor ReadTable(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return cursor;
    }
    public boolean DeleteTable(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,"id="+id,null);
        return true;
    }
    public boolean UpdateTable(String id,String task1){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DATABASE_NAME,task1);
        db.update(TABLE_NAME,cv,"id="+id,null);
        return true;
    }
}

