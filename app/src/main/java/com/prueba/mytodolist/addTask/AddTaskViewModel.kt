package com.prueba.mytodolist.addTask

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prueba.mytodolist.database.TaskEntry
import com.prueba.mytodolist.repository.TaskRepository

class AddTaskViewModel(taskId: Int, application: Application): ViewModel() {

    var task: LiveData<TaskEntry> = TaskRepository(application).getTaskById(taskId)
}