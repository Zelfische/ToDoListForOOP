package com.example.remaketodolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.remaketodolist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1; //database version
    private static final String NAME = "toDoListDatabase"; //database name
    private static final String TODO_TABLE = "todo"; //table name
    private static final String ID = "id"; //id row
    private static final String TASK = "task"; //text collumn
    private static final String STATUS = "status";//status
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK + " TEXT , " + STATUS + " INTEGER);"; //query for sql //id as primary key //status is int for 0 and 1
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE); //execute raw sql query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE); //Dropping older tables from todo_table
        onCreate(db); //Creating table once more
    }

    public void openDatabase() { //opendatabase method for working with the database
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {             //passing task which consist of status, id , text from todomodel class
        ContentValues cv = new ContentValues(); //naming contentvalues as cv
        cv.put(TASK, task.getTask()); //task is key
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv); //nullcolumnhack for passing the entire row //cv is the one incharge on inserting query instead of raw query
    }

    public List<ToDoModel> getAllTasks() { //using arraylist to send the class objecst to main activity
        List<ToDoModel> taskList = new ArrayList<>(); //new arraylist
        Cursor cur = null; //cursor object
        db.beginTransaction(); //return every criteria
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null); //return every rows in database
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndexOrThrow(ID))); //getting id from cursor //tried using get.columnindex but it will use a supress lint new API that notify if the app may crash
                        task.setTask(cur.getString(cur.getColumnIndexOrThrow(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));
                        taskList.add(task); //adding task on tasklist
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cur.close();
        }
        return taskList;
    }

    public void updateStatus (int id, int status){ //mark as done method
        ContentValues cv  = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)}); // ? forformatting //array for converting the id to string since id on model class is int then pass to ID column
    }

    public void updateTask(int id, String task){ //edit and update task from query
        ContentValues cv  = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv , ID + "=?" , new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){ //delete task from query
        db.delete(TODO_TABLE, ID + "=?" , new String[]{String.valueOf(id)});//passing id in array format new string
    }
}