package com.job.cm.libraryapp.repository

import com.job.cm.libraryapp.model.book.booking.Booking
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface BookingRepository : CoroutineCrudRepository<Booking, Int> {
    fun findAllByBookIdAndStatusEqualsOrderById(bookId: Int, status: Booking.Status): Flow<Booking>

    fun findAllByUserIdAndBookIdOrderById(userId: Int, bookId: Int): Flow<Booking>


    fun findAllByBookIdAndUserIdAndStatus(bookId: Int, userId: Int, status: Booking.Status): Flow<Booking>
}