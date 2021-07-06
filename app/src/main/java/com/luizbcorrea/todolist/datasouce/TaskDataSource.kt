package com.luizbcorrea.todolist.datasouce

import com.luizbcorrea.todolist.model.Task


object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun getList() = list.toList()

    fun insertTask(task: Task) {

        if (task.id == 0) {
            // se minha task.id for = a 0 então é uma nova task
            val rand_ID = (100000..999999).random()
            // id da task é randômico
            list.add(task.copy( id= rand_ID))

        } else {
            //se já tem um número de id então, remove a task com id antigo
            list.remove(task)

            // clona a task removida e insere um id novo
            val rand_ID_new = (100000..999999).random()
            list.add(task.copy(id= rand_ID_new))
        }
    }

     //pegando a lista e pegando o it.id comparando com a id da lista
    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun deleteTask(task: Task) {
        list.remove(task)
    }

}