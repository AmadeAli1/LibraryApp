package com.job.cm.libraryapp.model.book

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("Books")
data class Book(
    @Id
    @Column("ID")
    val id: Int? = null,
    @Column("AUTHORS")
    @field:NotBlank @field:Length(min = 2)
    val author: String,
    @Column("TITLE")
    @field:NotBlank @field:Length(min = 2)
    val title: String,
    @Column("PUBLISHER")
    @field:NotBlank @field:Length(min = 2)
    val publisher: String,
    @Column("YEAR_OF_PUBLISH")
    @field:NotNull
    @field:Min(1900)
    @field:Max(2023)
    val yearOfPublish: Int,
    @Column("NUMBER_OF_PAGES")
    @field:NotNull @field:Min(1)
    val numberOfPages: Int,
    @Column("ISBN")
    @field:NotBlank @field:Length(max = 13, min = 10)
    val isbn: String,
    @Column("TYPE")
    @field:NotNull
    val type: BookGenre,
    @Column("SUB_TYPE")
    var subType: BookGenre.SubType? = null,
    @Column("SYNOPSE")
    @field:NotBlank @field:Length(min = 10)
    val synopse: String,
    @Column("LANGUAGE")
    @field:NotNull
    val language: BookLanguage,
    @Column("QTY")
    @field:NotNull @field:PositiveOrZero
    val quantity: Int,
    @Column("AVAILABLE")
    val available: Boolean = quantity > 1,
    @Column("rating")
    @field:PositiveOrZero @field:Min(0) @field:Max(5)
    val rating: Int = 0,
    @field:JsonIgnore
    @Column("image")
    var image: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Book) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }

    fun toResponse(baseUrl:String): BookResponse {
        return BookResponse(
            id = id!!,
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
            rating = rating,
            imageUrl = "$baseUrl/api/storage/book/image/download?id=$id"
        )
    }
}
