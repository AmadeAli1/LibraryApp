package com.job.cm.libraryapp.model.book

import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length

data class BookRequest(
    @field:NotBlank @field:Length(min = 2)
    val author: String = "",
    @field:NotBlank @field:Length(min = 2)
    val title: String = "",
    @field:NotBlank @field:Length(min = 2)
    val publisher: String = "",
    @field:NotNull
    @field:Min(1900)
    @field:Max(2023)
    val yearOfPublish: Int = 0,
    @field:NotNull @field:Min(1)
    val numberOfPages: Int = 0,
    @field:NotBlank @field:Length(max = 13, min = 10)
    val isbn: String = "",
    @field:NotNull
    val type: String,
    var subType: String? = null,
    @field:NotBlank @field:Length(min = 10)
    val synopse: String = "",
    @field:NotNull
    val language: BookLanguage = BookLanguage.Portuguese,
    @field:NotNull @field:PositiveOrZero
    val quantity: Int = 0,
    val available: Boolean = quantity > 1,
    @field:PositiveOrZero @field:Min(0) @field:Max(5)
    val rating: Int,
    @field:NotBlank
    val imageUrl: String,
    @field:NotNull
    val price: Int,
) {

    fun toBook(): Book {
        return Book(
            id = null,
            author = author,
            title = title,
            publisher = publisher,
            yearOfPublish = yearOfPublish,
            numberOfPages = numberOfPages,
            isbn = isbn,
            type = type,
            subType = subType,
            synopse = synopse,
            language = language,
            quantity = quantity,
            available = available,
            imageUrl = imageUrl,
            price = price
        )
    }
}
