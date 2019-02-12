package com.prueba.mytodolist.addTask;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int mTaskId;
    private final Application mApplication;

    public AddTaskViewModelFactory(int taskId,  Application application) {
        mTaskId = taskId;
        mApplication = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddTaskViewModel(mTaskId, mApplication);
    }
}
