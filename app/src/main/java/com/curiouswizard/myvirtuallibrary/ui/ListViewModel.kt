package com.curiouswizard.myvirtuallibrary.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.curiouswizard.myvirtuallibrary.database.getDatabase
import com.curiouswizard.myvirtuallibrary.model.Book
import com.curiouswizard.myvirtuallibrary.repository.LibraryRepository

class ListViewModel(application: Application): ViewModel() {
    private val database = getDatabase(application)
    private val libraryRepository = LibraryRepository(database)

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()
    val showNoDataImg: MutableLiveData<Boolean> = MutableLiveData()
    val books: LiveData<List<Book>> = libraryRepository.books

    // Encapsulated LiveData to handle navigation properly
    private val _navigateToDetails = MutableLiveData<Book?>()
    val navigateToDetails: LiveData<Book?> = _navigateToDetails

    fun displayBookDetails(book: Book) {
        _navigateToDetails.value = book
    }

    fun doneNavigating() {
        _navigateToDetails.value = null
    }

    fun invalidateNoDataIndicators() {
        showNoData.value = books.value == null || books.value!!.isEmpty()
        showNoDataImg.value = books.value == null || books.value!!.isEmpty()
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class ListViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}