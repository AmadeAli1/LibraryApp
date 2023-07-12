package com.job.cm.libraryapp.model.book.loan

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("BooksWithLoan")
data class BooksWithLoan(
    @Column("userId")
    val userId: Int,
    @Column("bookId")
    val bookId: Int,
    @Column("bookTitle")
    val bookTitle: String,
    @Column("bookType")
    val bookType: String,
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