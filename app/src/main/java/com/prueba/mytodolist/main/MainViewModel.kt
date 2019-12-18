package com.prueba.mytodolist.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prueba.mytodolist.database.TaskEntry
import com.prueba.mytodolist.repository.TaskRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val tasks: LiveData<List<TaskEntry>>

    init {
        val taskRepository = TaskRepository(application)
        tasks = taskRepository.getAllTasks()
    }

    fun getTasks(): LiveData<List<TaskEntry>> {
        return tasks
    }
}