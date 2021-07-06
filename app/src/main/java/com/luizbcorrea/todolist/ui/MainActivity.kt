package com.luizbcorrea.todolist.ui


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luizbcorrea.todolist.databinding.ActivityMainBinding
import com.luizbcorrea.todolist.datasouce.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListeners()
    }


    private fun insertListeners() {
        // vai startá uma activity e esta um resultado do on activity result
        binding.fab.setOnClickListener{
            startActivityForResult(Intent(
                    this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            //colocando o id da task
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }
        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    // config do RecycleView ( recebe os resultados)
    override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // verificando se é o mesmo request para atualizar a lista
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            updateList()
        }
    }

    // Atualizar o adapter na lista
    private fun updateList() {
        //adapter.submitList(TaskDataSource.getList())
       val list = TaskDataSource.getList()

        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
        adapter.submitList(list)
    }

    // requestCode
    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}