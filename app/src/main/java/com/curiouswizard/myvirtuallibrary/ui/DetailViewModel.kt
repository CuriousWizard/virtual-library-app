package com.curiouswizard.myvirtuallibrary.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.curiouswizard.myvirtuallibrary.database.getDatabase
import com.curiouswizard.myvirtuallibrary.model.Book
import com.curiouswizard.myvirtuallibrary.repository.LibraryRepository
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): ViewModel() {

    private val database = getDatabase(application)
    private val libraryRepository = LibraryRepository(database)

    val bookId: MutableLiveData<Long> = MutableLiveData()
    val book: MutableLiveData<Book> = MutableLiveData()

    fun getBookById(id: Long) {
        viewModelScope.launch {
            book.value = libraryRepository.getBookById(id)
        }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class DetailViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}