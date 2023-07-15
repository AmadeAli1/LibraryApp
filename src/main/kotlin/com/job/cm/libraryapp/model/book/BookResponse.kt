package com.job.cm.libraryapp.model.book

import org.springframework.data.relational.core.mapping.Column

data class BookResponse(
    val id: Int,
    val author: String,
    val title: String,
    val publisher: String,
    val yearOfPublish: Int,
    val numberOfPages: Int,
    val isbn: String,
    val type: String,
    var subType: String? = null,
    val synopse: String,
    val language: BookLanguage,
    val quantity: Int,
    val available: Boolean,
    val rating: Int,
    val imageUrl: String,
    val price: Int,
) {

}
