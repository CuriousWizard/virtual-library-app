package com.curiouswizard.myvirtuallibrary.network

import com.curiouswizard.myvirtuallibrary.Constants
import com.curiouswizard.myvirtuallibrary.model.BookEditions
import com.curiouswizard.myvirtuallibrary.model.NetworkBook
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private val retrofitJson = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

/**
 * Retrofit interface for JSON parse using Moshi
 */
interface MolyApiJsonService {
    @GET("book_by_isbn.json")
    fun getBookByIsbn(@Query("q") isbn: String, @Query("key") apiKey: String): Call<NetworkBook>

    @GET("book_editions/{id}.json")
    suspend fun getBookEditionYear(
        @Path("id") bookId: Int,
        @Query("key") apiKey: String
    ): BookEditions
}

object MolyApi {

    val molyApiJsonService: MolyApiJsonService by lazy {
        retrofitJson.create(MolyApiJsonService::class.java)
    }
}