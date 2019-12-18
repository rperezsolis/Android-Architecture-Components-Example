package com.prueba.mytodolist

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors(diskIO: Executor) {

    var mDiskIO = diskIO

    companion object {

        private var sInstance: AppExecutors? = null

        fun getInstance(): AppExecutors? {
            if (sInstance == null) {
                synchronized(AppExecutors::class) {
                    sInstance = AppExecutors(Executors.newSingleThreadExecutor())
                }
            }
            return sInstance
        }
    }
}