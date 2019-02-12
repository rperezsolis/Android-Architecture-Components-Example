package com.prueba.mytodolist.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.prueba.mytodolist.addTask.AddTaskViewModelFactory;
import com.prueba.mytodolist.database.AppDatabase;
import com.prueba.mytodolist.database.TaskDao;
import com.prueba.mytodolist.database.TaskEntry;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;

    public TaskRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        taskDao = database.taskDao();
    }

    public LiveData<List<TaskEntry>> getAllTasks(){
        return taskDao.loadAllTasks();
    }

    public LiveData<TaskEntry> getTaskById(int taskId){
        return taskDao.loadTaskById(taskId);
    }

    public void insertTask(TaskEntry taskEntry){
        taskDao.insertTask(taskEntry);
    }

    public void updateTask(TaskEntry taskEntry){
        taskDao.updateTask(taskEntry);
    }

    public void deleteTask(TaskEntry taskEntry){
        taskDao.deleteTask(taskEntry);
    }

    public AddTaskViewModelFactory setAddTaskViewModel(int taskId, Application application){
        return new AddTaskViewModelFactory(taskId, application);
    }
}
