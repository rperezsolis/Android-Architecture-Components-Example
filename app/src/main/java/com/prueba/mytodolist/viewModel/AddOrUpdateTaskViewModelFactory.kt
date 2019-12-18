package com.prueba.mytodolist.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddOrUpdateTaskViewModelFactory(taskId: Int, application: Application): ViewModelProvider.NewInstanceFactory() {

    private var mTaskId: Int = taskId
    private var mApplication: Application = application

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddOrUpdateTaskViewModel(mTaskId, mApplication) as T
    }

    companion object {
        fun setAddTaskViewModel(taskId: Int, application: Application): AddOrUpdateTaskViewModelFactory {
            return AddOrUpdateTaskViewModelFactory(taskId, application)
        }
    }
}