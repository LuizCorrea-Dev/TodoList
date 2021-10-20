package com.luizbcorrea.todolist.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.luizbcorrea.todolist.database.models.Task

//  responsável por colocar e tirar dados da entidade
@Dao
interface TaskDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM todoTable WHERE date LIKE :searchQuery")
    fun searchDataBase(searchQuery: String): LiveData<List<Task>>

    @Query("SELECT * from todoTable where id Like :id")
    fun getTaskById(id: Long): LiveData<Task?>

    //---------------UPDATE-------------------------/
        @Update
        suspend fun updateTask(task: Task)

    //---------------CONTAGEM-------------------------/
        @Query("SELECT * FROM todoTable ORDER BY id DESC")
        fun allTasks(): LiveData<List<Task>>


        // Contar os itens tem na tabela task
        @Query("SELECT COUNT(id) FROM todoTable")
        suspend fun getTotalItems() : Long

    //---------------LISTAS-------------------------/

        @Query("SELECT * FROM todoTable ORDER BY date ASC")
        fun getList(): LiveData<List<Task>>

        // gerando lista de task em ordem alfabética
        @Query("SELECT * FROM todoTable ORDER BY title ASC")
        fun getAlphabetizedTasks():List<Task>

        // gerando lista de task em ordem só de data
        @Query("SELECT * FROM todoTable ORDER BY date ASC")
        fun getTasksByDate():List<Task>

        // gerando lista de task em ordem de hora e data
        @Query("SELECT * FROM todoTable ORDER BY date, hour ASC")
        fun getTasksByDateAndHour():List<Task>

    //---------------DELETE-------------------------/

        @Delete
        suspend fun deleteTask(task: Task)

        // deletar todas as tasks
        @Query("DELETE FROM todoTable")
        fun deleteAll()

    //---------------isDONE-------------------------/
/*
        // listar tasks pendentes
        @Query("SELECT * FROM todoTable where done = 0 ORDER BY date, hour ASC")
        fun getNotDone():List<Task>

        // listar tasks finalizadas
        @Query("SELECT * FROM todoTable where done = 1 ORDER BY date, hour ASC")
        fun getIsDone():List<Task>
*/

}