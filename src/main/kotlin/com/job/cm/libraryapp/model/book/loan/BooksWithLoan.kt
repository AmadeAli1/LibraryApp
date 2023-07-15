package com.job.cm.libraryapp.model.book.loan

import com.fasterxml.jackson.annotation.JsonFormat
import com.job.cm.libraryapp.model.book.BookLanguage
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("BooksWithLoan")
data class BooksWithLoan(
    @Column("bookId")
    val id: Int,
    @Column("userId")
    val userId: Int,
    @Column("bookTitle")
    val title: String,
    @Column("bookType")
    val type: String,
    @Column("PUBLISHER")
    @field:NotBlank @field:Length(min = 2)
    val publisher: String,
    @Column("YEAR_OF_PUBLISH")
    val yearOfPublish: Int,
    @Column("NUMBER_OF_PAGES")
    @field:NotNull @field:Min(1)
    val numberOfPages: Int,
    @Column("ISBN")
    @field:NotBlank @field:Length(max = 13, min = 10)
    val isbn: String,
    @Column("SUB_TYPE")
    var subType: String? = null,
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
    @Column("imageUrl")
    val imageUrl: String,
    @Column("price")
    val price: Int,
    @Column("comment")
    val comment: String,
    @Column("loanDate")
    @field:JsonFormat(pattern = "dd MMMM yyyy")
    val loanDate: LocalDateTime,
    @Column("returnDate")
    @field:JsonFormat(pattern = "dd MMMM yyyy")
    val returnDate: LocalDateTime,
    @Column("loanStatus")
    val loanStatus: String,
)