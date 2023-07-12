@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.job.cm.libraryapp.repository

import com.job.cm.libraryapp.model.book.Book
import com.job.cm.libraryapp.model.book.Bookmark
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookmarkRepository : CoroutineCrudRepository<Bookmark, Int> {
    suspend fun existsBookmarkByBookIdAndUserId(bookId: Int, userId: Int): Boolean
    suspend fun deleteBookmarkByBookIdAndUserId(bookId: Int, userId: Int): Int

    @Query("select * from userbookmark where userid=$1")
    fun findAllByUserId(userId: Int): Flow<Book>

}