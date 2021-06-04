package com.curiouswizard.myvirtuallibrary.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.curiouswizard.myvirtuallibrary.model.Book

@Dao
interface BookDao {

    @Query("SELECT * FROM books_table")
    fun getBooks(): LiveData<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)
}