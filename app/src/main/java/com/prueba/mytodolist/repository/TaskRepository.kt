package com.prueba.mytodolist.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.prueba.mytodolist.database.AppDatabase
import com.prueba.mytodolist.database.TaskDao
import com.prueba.mytodolist.model.TaskEntry

class TaskRepository(context: Context) {

    private lateinit var taskDao: TaskDao

    init {
        val database: AppDatabase? = AppDatabase.getInstance(context)
        if (database != null) taskDao = database.taskDao()
    }

    fun getAllTasks(): LiveData<List<TaskEntry>> {
        return taskDao.loadAllTasks()
    }

    fun getTaskById(taskId: Int): LiveData<TaskEntry> {
        return taskDao.loadTaskById(taskId)
    }

    fun insertTask(taskEntry: TaskEntry) {
        taskDao.insertTask(taskEntry)
    }

    fun updateTask(taskEntry: TaskEntry) {
        taskDao.updateTask(taskEntry)
    }

    fun deleteTask(taskEntry: TaskEntry) {
        taskDao.deleteTask(taskEntry)
    }
}