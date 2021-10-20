package com.luizbcorrea.todolist.database

import android.content.Context
import androidx.room.*
import com.luizbcorrea.todolist.database.daos.TaskDao
import com.luizbcorrea.todolist.database.models.Task

@Database( entities = [Task::class], version = 1,exportSchema = false)
abstract class TodoListDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao


    companion object {
        @Volatile
        private var INSTANCE: TodoListDatabase? = null

        fun getDatabase(ctx: Context): TodoListDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    TodoListDatabase::class.java,
                    "todoTable"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
