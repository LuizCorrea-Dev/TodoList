package com.luizbcorrea.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.luizbcorrea.todolist.R
import com.luizbcorrea.todolist.database.models.Task
import com.luizbcorrea.todolist.database.models.TodoViewModel
import com.luizbcorrea.todolist.databinding.ActivityAddTaskBinding
import com.luizbcorrea.todolist.extensions.format
import com.luizbcorrea.todolist.extensions.text
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var mTodoViewModel: TodoViewModel
    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        mTodoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        setupListeners()

        val taskId = intent.getLongExtra(TASK_ID, 0)

        if (taskId > 0) {
            binding.btnNewTask.text = getString(R.string.label_button_update_task)
            binding.toolbar.title = getString(R.string.label_button_update_task)
        } else {
            binding.btnNewTask.text = getString(R.string.label_button_create_task)
            binding.toolbar.title = getString(R.string.label_button_create_task)
        }

        setupObservers(taskId)

        binding.tilTitle.requestFocus()
    }

    private fun setupListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
//                val timeZone = TimeZone.getDefault()
//                val offset = timeZone.getOffset(Date().time) * -1
//                binding.tilDate.text = Date(it * offset).format()
                binding.tilDate.text = Date(it).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancelTask.setOnClickListener {
            finish()
        }

        binding.btnNewTask.setOnClickListener {
            if (binding.tilTitle.text.isBlank()) {
                binding.tilTitle.editText?.error = resources
                    .getString(R.string.label_field_mandatory)
            } else {
                binding.tilTitle.editText?.error = null
                val task = Task(
                    title = binding.tilTitle.text,
                    description = binding.tilDescription.text,
                    hour = binding.tilHour.text,
                    date = binding.tilDate.text,
                    id = intent.getLongExtra(TASK_ID, 0)
                )

                mTodoViewModel.insertTask(task)

                setResult(Activity.RESULT_OK)
                finish()

            }
        }

    }

    private fun setupObservers(taskId: Long) {
        mTodoViewModel.getAllData.observe(this, {
            mTodoViewModel.checkDatabaseEmpty(it)

            mTodoViewModel.findTaskById(taskId)?.let { task ->
                binding.tilTitle.text = task.title
                binding.tilDescription.text = task.description
                binding.tilDate.text = task.date
                binding.tilHour.text = task.hour
            }
        })
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
