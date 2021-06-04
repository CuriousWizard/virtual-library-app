package com.curiouswizard.myvirtuallibrary.network

import com.curiouswizard.myvirtuallibrary.Constants
import com.curiouswizard.myvirtuallibrary.model.BookEditions
import com.curiouswizard.myvirtuallibrary.model.NetworkBook
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
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

    /**
     * Returning as Response<Network> to be able to check response metadata.
     * For more info check:
     * https://github.com/square/retrofit/blob/master/CHANGELOG.md#version-260-2019-06-05
     */
    @GET("book_by_isbn.json")
    suspend fun getBookByIsbn(
        @Query("q") isbn: String,
        @Query("key") apiKey: String
    ): Response<NetworkBook>

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

