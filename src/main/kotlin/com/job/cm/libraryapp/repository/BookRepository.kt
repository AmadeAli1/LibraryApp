@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.job.cm.libraryapp.repository

import com.job.cm.libraryapp.model.book.Book
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CoroutineCrudRepository<Book, Int?> {
    suspend fun findBookByIsbn(isbn: String): Book?
    fun findAllByAvailable(available: Boolean): Flow<Book>

}