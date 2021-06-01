package com.curiouswizard.myvirtuallibrary.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NetworkBook(
    val id: Int,
    val author: String,
    val title: String,
    val cover: String
): Parcelable

@Parcelize
data class BookEditions(
    val editions: List<BookEdition>
): Parcelable

@Parcelize
data class BookEdition (
    val id: Int,
    val isbn: String? = null,
    val publisher: String,
    val place: String,
    val year: UInt,
    val pages: UShort,
    val cover: String
): Parcelable