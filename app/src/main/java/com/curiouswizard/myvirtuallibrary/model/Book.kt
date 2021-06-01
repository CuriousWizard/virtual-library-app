package com.curiouswizard.myvirtuallibrary.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "books_table")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val isbn: String,
    val authors: String,
    val title: String,
    val year: Short
): Parcelable