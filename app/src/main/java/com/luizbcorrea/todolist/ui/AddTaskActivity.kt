package com.luizbcorrea.todolist.ui
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luizbcorrea.todolist.databinding.ActivityAddTaskBinding
import com.luizbcorrea.todolist.datasouce.TaskDataSource
import com.luizbcorrea.todolist.extensions.format
import com.luizbcorrea.todolist.extensions.text
import com.luizbcorrea.todolist.model.Task
import java.util.*

class AddTaskActivity : AppCompatActivity(){


    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)

            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDescription.text = it.description
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour

            }
        }
        insertListeners()
    }

    private fun insertListeners() {



        // escutando a data de ADD.TASK
        binding.tilDate.editText?.setOnClickListener {

            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {

                // correção de timezone
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1

                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        // escutando a hora de ADD.TASK
        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .build()

            timePicker.addOnPositiveButtonClickListener {
                // correção de hora e minutos para aparecer 2 digitos
               val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
               val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }

        // botão cancelar
        binding.btnCancelTask.setOnClickListener {
            finish()
        }

        // botão adicionar
        binding.btnNewTask.setOnClickListener {
            val task = Task(
                    title = binding.tilTitle.text,
                    description =binding.tilDescription.text,
                    date = binding.tilDate.text,
                    hour = binding.tilHour.text,

                    // inserindo o id na task
                    id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
