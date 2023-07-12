@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.job.cm.libraryapp.repository

import com.job.cm.libraryapp.model.book.loan.BookLoaned
import com.job.cm.libraryapp.model.book.loan.BooksWithLoan
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface LoanRepository : CoroutineCrudRepository<BookLoaned, Int> {

    @Query("select * from bookswithloan where userid=$1")
    fun findAllBooksWithLoan(userId: Int): Flow<BooksWithLoan>


    suspend fun findByBookIdAndUserIdAndStatusEquals(bookId: Int, userId: Int, status: BookLoaned.LoanStatus): BookLoaned?
}