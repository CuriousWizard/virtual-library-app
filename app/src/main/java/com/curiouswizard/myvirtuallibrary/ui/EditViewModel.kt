package com.curiouswizard.myvirtuallibrary.ui

import android.app.Application
import androidx.lifecycle.*
import com.curiouswizard.myvirtuallibrary.R
import com.curiouswizard.myvirtuallibrary.database.getDatabase
import com.curiouswizard.myvirtuallibrary.model.Book
import com.curiouswizard.myvirtuallibrary.repository.LibraryRepository
import kotlinx.coroutines.launch
import java.util.*

class EditViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val libraryRepository = LibraryRepository(database)

    val snackbarMessageInt: MutableLiveData<Int> = MutableLiveData()
    val book: MutableLiveData<Book> = MutableLiveData()

    val title: MutableLiveData<String> = MutableLiveData()
    val authors: MutableLiveData<String> = MutableLiveData()
    val year: MutableLiveData<String> = MutableLiveData()
    val isbn: MutableLiveData<String> = MutableLiveData()

    // Encapsulated LiveData to handle navigation properly
    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?> = _navigateBack

    fun setup() {
        title.value = book.value?.title
        authors.value = book.value?.authors
        year.value = book.value?.year.toString()
        isbn.value = book.value?.isbn
    }

    fun editSelectedBook() = viewModelScope.launch {
        if(checkFields()){
            val newBook = Book(
                book.value!!.id,
                isbn.value,
                authors.value!!,
                title.value!!,
                year.value!!.toShort(),
                book.value!!.coverPhoto
            )
            libraryRepository.editBookById(newBook)
            navigateBack()
        }
    }

    private fun navigateBack() {
        _navigateBack.value = true
    }

    fun doneNavigatingBack(){
        _navigateBack.value = null
    }

    private fun checkFields(): Boolean {
        if (authors.value!!.isEmpty()) {
            snackbarMessageInt.postValue(R.string.empty_author)
            return false
        }
        if (title.value!!.isEmpty()){
            snackbarMessageInt.postValue(R.string.empty_title)
            return false
        }
        if (year.value!!.isEmpty() ||
            year.value!!.toInt() > Calendar.getInstance().get(Calendar.YEAR) ||
            year.value!!.toInt() < 0) {
            snackbarMessageInt.postValue(R.string.empty_year)
            return false
        }
        return true
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class EditViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}