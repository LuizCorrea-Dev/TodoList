package com.luizbcorrea.todolist.database

import androidx.lifecycle.LiveData
import com.luizbcorrea.todolist.database.daos.TaskDao
import com.luizbcorrea.todolist.database.models.Task

class TodoListRepository(private val taskDao: TaskDao) {

    val getList: LiveData<List<Task>> = taskDao.getList()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    fun getTaskById(id: Long): LiveData<Task?> {
        return taskDao.getTaskById(id)
    }

    fun searchByDateQuery(searchDate: String): LiveData<List<Task>> {
        return taskDao.searchDataBase(searchDate)
    }
}