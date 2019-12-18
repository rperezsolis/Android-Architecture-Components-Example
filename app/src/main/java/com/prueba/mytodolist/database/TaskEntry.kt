package com.prueba.mytodolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task")
data class TaskEntry (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    val description: String,

    val priority: Int,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date
)