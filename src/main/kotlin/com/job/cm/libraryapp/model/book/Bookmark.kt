package com.job.cm.libraryapp.model.book

import jakarta.validation.constraints.*
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("Bookmark")
data class Bookmark(
    @Id
    @Column("id")
    val id: Int? = null,
    @Column("userId")
    val userId: Int,
    @Column("bookId")
    val bookId: Int,
) {


    @Table("UserBookmark")
    data class UserBookmark(
        @Column("id")
        val id: Int,
        @Column("AUTHORS")
        val author: String,
        @Column("TITLE")
        val title: String,
        @Column("PUBLISHER")
        val publisher: String,
        @Column("YEAR_OF_PUBLISH")
        val yearOfPublish: Int,
        @Column("NUMBER_OF_PAGES")
        val numberOfPages: Int,
        @Column("ISBN")
        val isbn: String,
        @Column("TYPE")
        val type: BookGenre,
        @Column("SUB_TYPE")
        var subType: BookGenre.SubType? = null,
        @Column("SYNOPSE")
        val synopse: String,
        @Column("LANGUAGE")
        val language: BookLanguage,
        @Column("QTY")
        val quantity: Int,
        @Column("AVAILABLE")
        val available: Boolean = quantity > 1,
        @Column("rating")
        val rating: Int = 0,
    )

}
