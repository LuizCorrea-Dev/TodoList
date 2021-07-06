package com.luizbcorrea.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.luizbcorrea.todolist.R
import com.luizbcorrea.todolist.databinding.ItemTaskBinding
import com.luizbcorrea.todolist.model.Task

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskViewHolder
        >(DiffCallback()){

    lateinit var database: DatabaseReference

    //LAMBIDAS FUNCTIONS
    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : TaskViewHolder {

        // pegando o layout inflater no contexto da view
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
            private val binding:ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
            binding.tvDate.text = item.date
            binding.tvHour.text = item.hour
            binding.ivMore.setOnClickListener {
                showPopup(item)
            }
        }

        private fun showPopup(item: Task) {

            database= FirebaseDatabase.getInstance().reference

            val ivMore = binding.ivMore // icone

            //popup menu chamado pelo icone more
            val popupMenu = PopupMenu(ivMore.context, ivMore)

            //popupMenu recebendo o menu
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener{

                when (it.itemId) {
                    R.id.action_Edit -> listenerEdit(item)
                    R.id.action_Delete -> listenerDelete(item)
                }

                // captudando o evento de click do usuário
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}


class DiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(
        oldItem: Task, newItem: Task)=
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Task, newItem: Task) =
        oldItem.id == newItem.id

}