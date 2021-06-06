package com.curiouswizard.myvirtuallibrary.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.curiouswizard.myvirtuallibrary.BuildConfig
import com.curiouswizard.myvirtuallibrary.database.AppDatabase
import com.curiouswizard.myvirtuallibrary.model.Book
import com.curiouswizard.myvirtuallibrary.model.BookEditions
import com.curiouswizard.myvirtuallibrary.model.NetworkBook
import com.curiouswizard.myvirtuallibrary.network.MolyApi

class LibraryRepository(private val database: AppDatabase) {
    val books: LiveData<List<Book>> = database.bookDao.getBooks()
    val message: MutableLiveData<String> = MutableLiveData()
    val scannedBook: MutableLiveData<Book> = MutableLiveData()
    val years: MutableLiveData<List<String>> = MutableLiveData()

    private var actualIsbn: String = ""
    private var networkBook: NetworkBook? = null


    suspend fun getBookInfo(isbn: String) {
        getNetworkBook(isbn, true)
        if (networkBook != null) {
            val editions = getBookEditions(networkBook!!.id)
            val yearsList: MutableList<String> = mutableListOf()
            for (edition in editions.editions) {
                if (actualIsbn == edition.isbn) {
                    yearsList.add(edition.year.toString())
                }
            }

            years.postValue(yearsList)
            scannedBook.postValue(
                Book(
                    0L,
                    actualIsbn,
                    networkBook!!.author,
                    networkBook!!.title,
                    0,
                    networkBook!!.cover.replace("normal", "big")
                )
            )
        }
    }

    private suspend fun getNetworkBook(isbn: String, firstTry: Boolean) {
        val response = MolyApi.molyApiJsonService.getBookByIsbn(isbn, BuildConfig.MOLY_API_KEY)
        if (firstTry) {
            when (response.isSuccessful) {
                true -> {
                    // We found the book on API
                    networkBook = response.body()
                    actualIsbn = isbn
                    message.postValue("Found your book's details")
                }
                false -> {
                    if (response.code() == 403) {
                        getNetworkBook(isbn.substring(3), false)
                    }
                }
            }
        } else {
            when (response.isSuccessful) {
                true -> {
                    // We found the book on API
                    networkBook = response.body()
                    actualIsbn = isbn
                    message.postValue("Found your book's details")
                }
                false -> {
                    if (response.code() == 403) {
                        // Could not find on second try
                        message.postValue("Could not find details about your book on the server")
                    } else {
                        // If failed to get any response from the server (eg. API is down)
                        message.postValue("Something went wrong while connecting to server")
                    }
                }
            }
        }
    }

    suspend fun editBookById(book: Book){
        database.bookDao.editBookById(book)
    }

    private suspend fun getBookEditions(id: Int): BookEditions =
        MolyApi.molyApiJsonService.getBookEditionYear(id, BuildConfig.MOLY_API_KEY)

    suspend fun saveBookToDatabase(book: Book) {
        database.bookDao.insert(book)
    }
}