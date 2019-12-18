package com.prueba.mytodolist.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.prueba.mytodolist.AppExecutors
import com.prueba.mytodolist.model.TaskEntry
import com.prueba.mytodolist.repository.TaskRepository
import java.util.*

class AddOrUpdateTaskViewModel(taskId: Int, application: Application): ViewModel() {

    companion object {
        const val DEFAULT_TASK_ID = -1
    }

    private val taskRepository: TaskRepository = TaskRepository(application)
    private val task: LiveData<TaskEntry> = taskRepository.getTaskById(taskId)

    fun getTask(): LiveData<TaskEntry> {
        return task
    }

    private fun insertTask(task: TaskEntry) {
        taskRepository.insertTask(task)
    }

    private fun updateTask(task: TaskEntry) {
        taskRepository.updateTask(task)
    }

    fun insertOrUpdateTask(taskId: Int, description: String, priority: Int, date: Date) {
        val task = TaskEntry(0, description, priority, date)
        AppExecutors.getInstance()?.mDiskIO?.execute {
            if (taskId == DEFAULT_TASK_ID) {
                insertTask(task)
            } else {
                task.id = taskId
                updateTask(task)
            }
        }
    }
}