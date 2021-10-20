package com.luizbcorrea.todolist.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luizbcorrea.todolist.databinding.ItemFilterDayBinding
import java.text.SimpleDateFormat
import java.util.*

class DatesFilterListAdapter : ListAdapter<String, DatesFilterListAdapter.DateFilterViewHolder>(DiffCallbackFilter()) {

    private var dateSelected: String = ""

    var listenerClick: (String) -> Unit = {}
    var dateSelectedClean: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateFilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFilterDayBinding.inflate(inflater, parent, false)
        return DateFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DateFilterViewHolder(
        private val binding: ItemFilterDayBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

            val itemDate: String = item
            val strDayOfWeek = formatStringToDayOfWeek(item)

            formatChosenDate(itemDate, dateSelected)

            binding.tvWeekday.text = strDayOfWeek
            binding.tvDay.text = item.take(LENGTH_TWO)

            binding.cvDayFilter.setOnClickListener {
                listenerClick(item)
                dateSelected = item
                notifyDataSetChanged()
            }

            dateSelectedClean = {
                dateSelected = ""
            }
        }

        fun formatStringToDayOfWeek(dateItem: String): String {
            val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date: Date = sdf.parse(dateItem) ?: Date()
            val sdfDayOfWeek: SimpleDateFormat =
                SimpleDateFormat("dd/MM/yyyy E", Locale.getDefault())
            val dateFormatted = sdfDayOfWeek.format(date).split(" ")
            return dateFormatted[INDEX_1].replace(".","")
        }

        fun formatChosenDate(date: String, currentDate: String) {
            if(date == currentDate) {
                binding.clDayFilter.setBackgroundColor(Color.RED)
                binding.tvDay.setTextColor(Color.WHITE)
                binding.tvWeekday.setTextColor(Color.WHITE)
                binding.viewDivider.setBackgroundColor(Color.WHITE)
            } else {
                binding.clDayFilter.setBackgroundColor(Color.WHITE)
                binding.tvDay.setTextColor(Color.BLACK)
                binding.tvWeekday.setTextColor(Color.BLACK)
                binding.viewDivider.setBackgroundColor(Color.RED)
            }
        }
    }

    companion object {
        const val LENGTH_TWO = 2
        const val INDEX_1 = 1
    }
}

/**
 * Usado para adicionar regras de validação dos itens, que podemos personalizar se assim quiser
 */
class DiffCallbackFilter : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

