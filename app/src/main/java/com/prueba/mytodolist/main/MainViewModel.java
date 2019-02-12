package com.prueba.mytodolist.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.prueba.mytodolist.database.TaskEntry;
import com.prueba.mytodolist.repository.TaskRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<TaskEntry>> tasks;

    public MainViewModel(Application application) {
        super(application);
        //AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        //tasks = database.taskDao().loadAllTasks();
        TaskRepository taskRepository = new TaskRepository(application);
        tasks = taskRepository.getAllTasks();
    }

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }
}