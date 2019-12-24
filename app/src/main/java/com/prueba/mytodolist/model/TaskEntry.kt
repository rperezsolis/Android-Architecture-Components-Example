package com.prueba.mytodolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "task")
data class TaskEntry (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    val description: String,

    val priority: Int,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date,

    val deadline: Date?
)