@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.job.cm.libraryapp.repository

import com.job.cm.libraryapp.model.book.Book
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CoroutineCrudRepository<Book, Int?> {
    suspend fun findBookByIsbn(isbn: String): Book?
    fun findAllByAvailable(available: Boolean): Flow<Book>

    @Query("select * from books where upper(authors) like upper(concat($1,'%')) or upper(title) like upper(concat($1,'%')) or upper(publisher) like upper(concat($1,'%')) or upper(type) like (upper(concat($1,'%')))")
    fun search(query: String): Flow<Book>

    fun findAllByRating(rating: Int): Flow<Book>

}