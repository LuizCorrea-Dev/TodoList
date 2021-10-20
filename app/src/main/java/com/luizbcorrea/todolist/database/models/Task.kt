package com.luizbcorrea.todolist.database.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// INFORMANDO QUE SE TRATA DE UMA TABELA
@Entity (tableName = "todoTable")

@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var title: String,
    var description: String,
    var date: String,
    var hour: String
): Parcelable