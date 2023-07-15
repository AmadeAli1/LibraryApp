package com.job.cm.libraryapp.model.book.booking

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("Bookings")
data class Booking(
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
    @Column("STATUS")
    val status: Status = Status.Reservado,
) {

    enum class Status {
        Concluido, Reservado
    }
}
