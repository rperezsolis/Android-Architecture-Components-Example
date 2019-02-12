package com.prueba.mytodolist.addTask;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.prueba.mytodolist.database.TaskEntry;
import com.prueba.mytodolist.repository.TaskRepository;

public class AddTaskViewModel extends ViewModel {

    private LiveData<TaskEntry> task;

    public AddTaskViewModel(int taskId, Application application) {
        TaskRepository taskRepository = new TaskRepository(application);
        task = taskRepository.getTaskById(taskId);
    }

    public LiveData<TaskEntry> getTask() {
        return task;
    }
}