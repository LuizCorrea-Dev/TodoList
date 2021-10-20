package com.luizbcorrea.todolist.database.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luizbcorrea.todolist.database.TodoListDatabase
import com.luizbcorrea.todolist.database.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = TodoListDatabase.getDatabase(application).taskDao()
    private val repository: TodoListRepository

    private val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    val getAllData: LiveData<List<Task>>

    init {
        repository = TodoListRepository(taskDao)
        getAllData = repository.getList
    }

    fun checkDatabaseEmpty(todoList: List<Task>) {
        emptyDatabase.value = todoList.isEmpty()
    }

    val filterDates: MutableLiveData<MutableList<String>> by lazy {
        MutableLiveData<MutableList<String>>()
    }

    fun insertTask(task: Task) {
        if (task.id.toInt() == 0) {
            viewModelScope.launch(Dispatchers.IO) { repository.insertTask(task) }
        } else {
            updateTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { repository.updateTask(task) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteTask(task) }
    }

    fun findTaskById(id: Long): Task? {
        getAllData.value?.forEach {
            if (it.id == id) {
                return it
            }
        }

        return null
    }

    fun searchByDate(date: String): LiveData<List<Task>> {
        return repository.searchByDateQuery(date)
    }

    fun updateFilterAllData() {
        filterDates.postValue(updateDatesFilter())
    }

    private fun updateDatesFilter(): MutableList<String> {
        val listFilter = getAllData.value?.distinctBy { it.date }
        val dates: MutableList<String> = mutableListOf()

        listFilter?.forEach {
            if (it.date.isNotEmpty()) {
                dates.add(it.date)
            }
        }

        return dates
    }

    fun getList(): List<Task> {
        return getAllData.value!!
    }
}

