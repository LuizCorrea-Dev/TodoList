package com.luizbcorrea.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.luizbcorrea.todolist.R
import com.luizbcorrea.todolist.database.models.Task
import com.luizbcorrea.todolist.database.models.TodoViewModel
import com.luizbcorrea.todolist.databinding.ActivityMainBinding
import com.luizbcorrea.todolist.ui.adapters.DatesFilterListAdapter
import com.luizbcorrea.todolist.ui.adapters.TaskListAdapter
import kotlinx.android.synthetic.main.item_task.*

class MainActivity : AppCompatActivity() {

    lateinit var mTaskViewModel: TodoViewModel

    private lateinit var binding: ActivityMainBinding
    private val adapterTasks by lazy { TaskListAdapter() }
    private val adapterFilter by lazy { DatesFilterListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapterTasks
        binding.rvDaysFilter.adapter = adapterFilter

        mTaskViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        setupListeners()
        visibleLoading()
        setupObservers()
    }

    private fun visibleLoading() {
        binding.includeStateLoading.clLoadingState.visibility = View.VISIBLE
    }

    private fun goneLoading() {
        binding.includeStateLoading.clLoadingState.visibility = View.GONE
    }

    private fun setupObservers() {
        mTaskViewModel.getAllData.observe(this, {
            mTaskViewModel.checkDatabaseEmpty(it)
            updateList(it)
            binding.tvFilterActive.text = getString(R.string.label_all_tasks_by_date_desc)
            mTaskViewModel.updateFilterAllData()
            goneLoading()
        })

        mTaskViewModel.filterDates.observe(this, Observer {
            updateDatesFilter(it)
        })
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            startActivityForResult(
                Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK
            )
        }

        adapterTasks.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, UPDATE_TASK)
        }

        adapterTasks.listenerDelete = {
            visibleLoading()
            mTaskViewModel.deleteTask(it)
        }

        adapterFilter.listenerClick = {
            visibleLoading()
            mTaskViewModel.searchByDate(it).observe(this, Observer { filteredList ->
                updateList(filteredList)
            })
            goneLoading()

            binding.tvFilterActive.text = getString(
                R.string.label_tasks_filtered_by_date,
                it
            )
            binding.buttonCleanFilter.isEnabled = true
        }

        binding.buttonCleanFilter.setOnClickListener {
            visibleLoading()
            updateList(mTaskViewModel.getList())
            mTaskViewModel.updateFilterAllData()
            goneLoading()
            binding.buttonCleanFilter.isEnabled = false
            binding.tvFilterActive.text = getString(R.string.label_all_tasks_by_date_desc)
        }
    }

    private fun updateList(list: List<Task>) {
        if (list.isEmpty()) {
            binding.includeState.emptyState.visibility = View.VISIBLE
            binding.tvTitle.visibility = View.GONE
            binding.buttonCleanFilter.visibility = View.GONE
        } else {
            binding.includeState.emptyState.visibility = View.GONE
            binding.tvTitle.visibility = View.VISIBLE
            binding.buttonCleanFilter.visibility = View.VISIBLE
        }

        adapterTasks.submitList(list)
        adapterTasks.notifyDataSetChanged()
    }

    private fun updateDatesFilter(list: MutableList<String>) {
        adapterFilter.dateSelectedClean()
        adapterFilter.submitList(list)
        adapterFilter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        visibleLoading()

        when {
            requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK -> {
                feedBackTaskActions(resources.getString(R.string.label_new_task_success))
            }

            requestCode == UPDATE_TASK && resultCode == Activity.RESULT_OK -> {
                feedBackTaskActions(resources.getString(R.string.label_update_task_success))
            }
        }

        updateList(mTaskViewModel.getAllData.value as List<Task>)
    }

    //Feedback actions
    private fun feedBackTaskActions(msg: String) {
        val snackbar = Snackbar.make(
            this, binding.tvTitle,
            msg, Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }
    companion object {
        private const val CREATE_NEW_TASK = 1000
        private const val UPDATE_TASK = 1001
    }
}