package com.example.remaketodolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.remaketodolist.AddNewTask;
import com.example.remaketodolist.MainActivity;
import com.example.remaketodolist.Model.ToDoModel;
import com.example.remaketodolist.R;
import com.example.remaketodolist.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList; //getting objects from todomodel class
    private MainActivity activity; //context
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity){ //pasing also the db
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){ //passing the viewgroup and int viewtype
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView); //returning itemView from the view in line 33
    }

    public void onBindViewHolder(ViewHolder holder, int position){ //implementing recyclerview
        db.openDatabase();
        ToDoModel item = todoList.get(position); //fromthe holder getting the postion from the arraylist todolist
        holder.task.setText(item.getTask()); //showing in the screen taking from holder from the task in model class after getting it from item
        holder.task.setChecked(toBoolean(item.getStatus())); //since the status is on int data type we need to make it on boolean
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //checking and unchecking updater in database
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else{
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public int getItemCount(){
        return todoList.size(); //required to let the recycler view know how many items it needs to print
    }

    private boolean toBoolean(int n){
        return n!=0;
    } //line 42

    public void setTask(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();//updating recyclerview
    }
    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position); //notify the recyclerview if something is removed
    }

    public void editItem(int position){
        ToDoModel item = todoList.get(position); //getting the item needs to be edit passing to bottomsheetfragment
        Bundle bundle = new Bundle(); // getting the item
        bundle.putInt("id", item.getId()); //passing the item or id
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task; //importing CHeckbox from task layout .xml

        ViewHolder(View view){ //constructor //passing the view object into View
            super(view); // used to remove the confusion in name XD
            task = view.findViewById(R.id.todoCheckBox); //passing task to the xml file named tasklayout
        }
    }

}