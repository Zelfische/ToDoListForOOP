package com.example.remaketodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.remaketodolist.Adapter.ToDoAdapter;
import com.example.remaketodolist.Model.ToDoModel;
import com.example.remaketodolist.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener { //inheritance of interface class named dialogcloselistener

    private RecyclerView taskRecyclerview;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> taskList;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();


        taskRecyclerview = findViewById(R.id.tasksRecyclerView);
        taskRecyclerview.setLayoutManager(new LinearLayoutManager(this)); //setting the recyclerview as linear layout
        tasksAdapter = new ToDoAdapter(db,this);
        taskRecyclerview.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerview);


        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTask(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

    }
    @Override
    public void handleDialogClose(DialogInterface dialog){ //polymorphed from interface class
        taskList = db.getAllTasks();
        Collections.reverse(taskList); //for recently added task will be moved to the top
        tasksAdapter.setTask(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}