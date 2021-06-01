package com.curiouswizard.myvirtuallibrary.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.curiouswizard.myvirtuallibrary.BuildConfig
import com.curiouswizard.myvirtuallibrary.model.NetworkBook
import com.curiouswizard.myvirtuallibrary.network.MolyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddViewModel : ViewModel() {

    // Encapsulated LiveData
    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> = _title

    private val _authors: MutableLiveData<String> = MutableLiveData()
    val authors: LiveData<String> = _authors

    private val _year: MutableLiveData<Short> = MutableLiveData()
    val year: LiveData<Short> = _year
    val yearString: LiveData<String> = Transformations.map(_year) { it.toString() }

    private val _isbn: MutableLiveData<String> = MutableLiveData()
    val isbn: LiveData<String> = _isbn

    private val _coverPhoto: MutableLiveData<String> = MutableLiveData()
    val coverPhoto: LiveData<String> = _coverPhoto

    val scannedIsbn: MutableLiveData<String> = MutableLiveData()

    private val _networkBookId: MutableLiveData<Int> = MutableLiveData()

    val snackbarMessage: MutableLiveData<String> = MutableLiveData()


    fun getBookInfo(isbn: String) {
        _isbn.value = isbn
        getNetworkBook(isbn, true)
    }

    private fun getNetworkBook(isbn: String, firstTry: Boolean){
        MolyApi.molyApiJsonService.getBookByIsbn(isbn, BuildConfig.MOLY_API_KEY)
            .enqueue(object: Callback<NetworkBook> {
                override fun onResponse(call: Call<NetworkBook>, response: Response<NetworkBook>) {
                    // Check if the server returns with HTTP 403 code.
                    // It means that it could not find the book with the given ISBN number
                    if (response.code() == 403) {
                        // If this is the first time to lookup ISBN number, try again with shorten ISBN
                        if (firstTry) {
                            // Creating ISBN-10 from ISBN-13 (exclude 978 prefix)
                            val newISBN = isbn.substring(3)
                            _isbn.postValue(newISBN)
                            getNetworkBook(newISBN, false)
                        } else {
                            snackbarMessage.postValue("Could not find details about your book on the server")
                        }
                    } else {
                        // We found book on API
                        setBookDetails(response.body()!!)
                        snackbarMessage.postValue("Found your book's details")
                    }
                }

                override fun onFailure(call: Call<NetworkBook>, t: Throwable) {
                    // If failed to get any response from the server (eg. API is down)
                    snackbarMessage.postValue("Something went wrong during internet connection")
                    Log.e("getNetworkBook", t.message.toString())
                }
            })
    }

    /**
     * Setting MutableLiveData values from the API response
     */
    private fun setBookDetails(book: NetworkBook){
        _networkBookId.postValue(book.id)
        _title.postValue(book.title)
        _authors.postValue(book.author)

        val bigCoverPhoto = book.cover.replace("normal", "big")
        _coverPhoto.postValue(bigCoverPhoto)
    }

}