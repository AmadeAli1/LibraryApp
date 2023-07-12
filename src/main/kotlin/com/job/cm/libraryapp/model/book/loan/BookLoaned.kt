package com.job.cm.libraryapp.model.book.loan

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("BOOKS_LOANED")
data class BookLoaned(
    @Id
    @Column("ID")
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val id: Int? = null,
    @Column("USER_ID")
    @field:NotNull
    val userId: Int,
    @Column("BOOK_ID")
    @field:NotNull
    val bookId: Int,
    @Column("LOAN_DATE")
    @field:JsonFormat(pattern = "dd MMMM yyyy")
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val loanDate: LocalDateTime = LocalDateTime.now(),
    @Column("RETURN_DATE")
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @field:JsonFormat(pattern = "dd MMMM yyyy")
    val returnDate: LocalDateTime = loanDate.plusWeeks(1),
    @Column("STATUS")
    @field:NotNull
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val status: LoanStatus = LoanStatus.NotDelivered,
    @Column("COMMENTS")
    @field:NotBlank
    val comment: String,
) {
    enum class LoanStatus {
        Delivered, NotDelivered
    }
}