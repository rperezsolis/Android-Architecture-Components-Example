package com.prueba.mytodolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.prueba.mytodolist.model.TaskEntry

@Database(entities = [TaskEntry::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(AppDatabase::class) {
                    sInstance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "todolist")
                            .addMigrations(TaskMigration1to2(1, 2))
                            .build()
                }
            }
            return sInstance
        }

        fun destroyInstance() {
            sInstance = null
        }

        private class TaskMigration1to2(previousVersion: Int, nextVersion: Int) : Migration(previousVersion, nextVersion) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task ADD COLUMN deadline INTEGER")
            }
        }
    }
}