package com.example.remaketodolist.Model;

public class ToDoModel {
    private int id, status; //task attributes status is boolean ocz in sql we cant use boolean data types for 1 is done and 0 is notyet done
    private String task; //text of task to get

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}

//made a new package for this class to make easier to find what classes i need when im trying to look for the class