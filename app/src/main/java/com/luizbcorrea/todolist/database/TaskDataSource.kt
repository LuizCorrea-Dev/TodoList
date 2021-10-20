package com.luizbcorrea.todolist.database

import com.luizbcorrea.todolist.database.models.Task

object TaskDataSource {
    private val list = mutableListOf<Task>()

    init {
        val listMock = mutableListOf<Task>(
            Task(1, "Titulo tarefa 1", "descricao do item", "29/06/2021", "18:00"),
            Task(2, "Titulo tarefa 2", "descricao do item", "01/01/2021", "18:00"),
            Task(3, "Titulo tarefa 3", "descricao do item", "02/01/2021", "18:00"),
            Task(4, "Titulo tarefa 4", "descricao do item", "01/01/2021", "18:00"),
            Task(5, "Titulo tarefa 5", "descricao do item", "02/01/2021", "18:00"),
            Task(5, "Titulo tarefa 6", "descricao do item", "02/01/2021", "18:00"),
            Task(6, "Titulo tarefa 7", "descricao do item", "01/01/2021", "18:00")
        )

        if(list.size == 0) {
            list.addAll(listMock)
        }
    }

    fun getList() = list
/*
    fun insertTask(task: Task) {
        if (task.id == 0) {
            list.add(task.copy(id = list.size + 1))
       } else {
            list.remove(task)
            list.add(task)
       }
    }

    fun findById(taskId: Int) = list.find { it.id == taskId }
*/
    fun delete(task: Task) {
        list.remove(task)
    }

    //Retorna datas registradas para filtro
    fun filterRecordedDates(): List<Task> = list.distinctBy { it.date }
}