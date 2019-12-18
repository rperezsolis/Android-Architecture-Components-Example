package com.prueba.mytodolist.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prueba.mytodolist.AppExecutors
import com.prueba.mytodolist.model.TaskEntry
import com.prueba.mytodolist.repository.TaskRepository
import com.prueba.mytodolist.view.adapter.TaskAdapter

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val tasks: LiveData<List<TaskEntry>>
    private val taskRepository: TaskRepository = TaskRepository(application)

    init {
        tasks = taskRepository.getAllTasks()
    }

    fun getTasks(): LiveData<List<TaskEntry>> {
        return tasks
    }

    fun deleteTask(position: Int, adapter: TaskAdapter) {
        AppExecutors.getInstance()?.mDiskIO?.execute {
            val tasks = adapter.getTasks()
            taskRepository.deleteTask(tasks[position])
        }
    }
}