package com.luizbcorrea.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import java.util.*
import java.text.SimpleDateFormat as SimpleDateFormat

private val locale = Locale("pt", "BR")

fun Date.format() : String{
    return SimpleDateFormat( "dd/MM/yyyy", locale).format(this)
}

var TextInputLayout.text: String
    get() = editText?.text.toString()?:""
    set(value){
        editText?.setText(value)
    }

fun Date.dayOfWeekBR(date: String): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val data = sdf.parse(date)
    val gc = GregorianCalendar()
    gc.setTime(data)
    val diaDaSemana: Int = gc.get(GregorianCalendar.DAY_OF_WEEK)

    return dayOfWeekPortugueseBr(diaDaSemana)
}

fun dayOfWeekPortugueseBr(day: Int): String {
    val daysOfWeekAbr = listOf<String>(
        "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"
    )
    return daysOfWeekAbr[day- NUMBER_ONE]
}

private const val NUMBER_ONE = 1