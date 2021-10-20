package com.luizbcorrea.todolist.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luizbcorrea.todolist.database.models.Task
import com.luizbcorrea.todolist.databinding.ItemTaskBinding


// implementando o recycler view adapter com viewHolder
class TaskListAdapter : ListAdapter<Task, TaskListAdapter.ViewHolder>(DiffCallback()){

    //Create functions empty type unit with param Task
    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    // este método é chamado todas as vezes que for criar um item da RV
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // suporte do layout para o RV ( popula as informaões)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    // implementação do ViewHolder
    inner class ViewHolder (
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task) {
            binding.tvTitle.text = item.title
            binding.tvDate.text = "${item.date} ${item.hour}"
            if (item.description.isNotBlank()) {
                binding.tvDescription.text = item.description
            } else {
                binding.tvDescription.visibility = View.GONE
            }

            binding.btnDeleteTask.setOnClickListener {
                listenerDelete(item)
            }

            binding.btnEditTask.setOnClickListener {
                listenerEdit(item)
            }
        }
    }


    // Usado para adicionar regras de validação dos itens, que podemos personalizar

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(
            oldItem: Task,
            newItem: Task)
        : Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Task, newItem: Task)
        : Boolean {
            return oldItem.id == newItem.id
        }

    }
}
