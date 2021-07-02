package com.curiouswizard.myvirtuallibrary.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.curiouswizard.myvirtuallibrary.model.Book

@Dao
interface BookDao {

    @Query("SELECT * FROM books_table")
    fun getBooks(): LiveData<List<Book>>

    @Query("SELECT * FROM books_table WHERE id=:id")
    suspend fun getBook(id: Long): Book

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Update
    suspend fun editBookById(book: Book)
}