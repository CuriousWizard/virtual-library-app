package com.curiouswizard.myvirtuallibrary.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.database.getDatabase
import com.curiouswizard.myvirtuallibrary.model.Book
import com.curiouswizard.myvirtuallibrary.repository.LibraryRepository
import kotlinx.coroutines.launch
import java.util.*

class AddViewModel(application: Application): ViewModel() {

    private val database = getDatabase(application)
    private val libraryRepository = LibraryRepository(database)

    val snackbarMessage = libraryRepository.message
    val scannedBook = libraryRepository.scannedBook
    val years = libraryRepository.years

    val scannedIsbn: MutableLiveData<String> = MutableLiveData()

    val title: MutableLiveData<String> = MutableLiveData("")
    val authors: MutableLiveData<String> = MutableLiveData("")
    val year: MutableLiveData<String> = MutableLiveData("")
    val isbn: MutableLiveData<String> = MutableLiveData("")

    // Encapsulated LiveData to handle navigation properly
    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?> = _navigateBack


    fun getBookInfo(isbn: String) = viewModelScope.launch {
        libraryRepository.getBookInfo(isbn)
    }

    fun setBookInfo(book: Book) {
        title.postValue(book.title)
        authors.postValue(book.authors)
        isbn.postValue(book.isbn)
    }

    fun saveBook(context: Context) = viewModelScope.launch  {
        if (authors.value!!.isEmpty()) {
            snackbarMessage.postValue(context.getString(R.string.empty_author))
        }
        if (title.value!!.isEmpty()){
            snackbarMessage.postValue(context.getString(R.string.empty_title))
        }
        if (year.value!!.isEmpty() ||
            year.value!!.toInt() > Calendar.getInstance().get(Calendar.YEAR) ||
            year.value!!.toInt() < 0) {
            snackbarMessage.postValue(context.getString(R.string.empty_year))
        }
        if (authors.value!!.isNotEmpty() && title.value!!.isNotEmpty() && year.value!!.isNotEmpty()){
            val book = Book(
                0L,
                isbn.value,
                authors.value!!,
                title.value!!,
                year.value!!.toShort(),
                scannedBook.value?.coverPhoto
            )
            libraryRepository.saveBookToDatabase(book)
            navigateBackToList()
        }
    }

    fun navigateBackToList() {
        _navigateBack.value = true
    }

    fun doneNavigating() {
        _navigateBack.value = null
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class AddViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}