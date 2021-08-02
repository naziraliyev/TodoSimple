package com.example.todosimple;

public class ToDoModel {
    String task;
    String date;

    public ToDoModel() {
    }

    public ToDoModel(String task, String date) {
        this.task = task;
        this.date = date;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
