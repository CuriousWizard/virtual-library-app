package com.curiouswizard.myvirtuallibrary.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.curiouswizard.myvirtuallibrary.model.Book

@Dao
interface BookDao {

    @Query("SELECT * FROM books_table")
    fun getBooks(): LiveData<List<Book>>
}