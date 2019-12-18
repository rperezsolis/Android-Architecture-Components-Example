package com.prueba.mytodolist.addTask

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddTaskViewModelFactory(taskId: Int, application: Application): ViewModelProvider.NewInstanceFactory() {

    private var mTaskId: Int = 0
    private var mApplication: Application

    init {
        mTaskId = taskId
        mApplication = application
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddTaskViewModel(mTaskId, mApplication) as T
    }
}