package com.job.cm.libraryapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.job.cm.libraryapp.exception.ApiException
import com.job.cm.libraryapp.model.book.BookRequest
import com.job.cm.libraryapp.model.book.BookResponse
import com.job.cm.libraryapp.model.book.loan.BookLoaned
import com.job.cm.libraryapp.model.book.loan.BooksWithLoan
import com.job.cm.libraryapp.service.BookService
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService,
    private val mapper: ObjectMapper,
) {

    @PostMapping("/post")
    suspend fun postBook(
        @RequestPart("image", required = true) image: FilePart,
        @RequestPart("book", required = true) body: String,
    ): Boolean {
        val bookRequest = try {
            mapper.readValue(body, BookRequest::class.java)
        } catch (e: Exception) {
            throw ApiException(e.message ?: e.toString())
        }
        return bookService.postBook(book = bookRequest.toBook(), image = image)
    }

    @GetMapping("/all")
    suspend fun findAll(): Flow<BookResponse> {
        return bookService.findAll()
    }

    @GetMapping("/available")
    suspend fun findAllWhereBookIsAvailable(): Flow<BookResponse> {
        return bookService.findAllAvailableBooks()
    }

    @GetMapping
    suspend fun findBookById(
        @RequestParam("id", required = true) bookId: Int,
    ): BookResponse {
        return bookService.findBookById(id = bookId)
    }

    @GetMapping("/loan/all")
    suspend fun findAllBooksWithLoan(@RequestParam("userId") userId: Int): Flow<BooksWithLoan> {
        return bookService.findAllBooksWithLoan(userId = userId)
    }

    @PostMapping("/loan")
    suspend fun loanBook(@Valid @RequestBody bookLoaned: BookLoaned): Boolean {
        return bookService.loanBook(bookLoaned)
    }

    @PostMapping("/delivery")
    suspend fun loanBook(
        @RequestParam("bookId") bookId: Int,
        @RequestParam("userId") userId: Int,
    ): Boolean {
        return bookService.deliveryBook(userId, bookId)
    }


    @GetMapping("/bookmarks")
    suspend fun findBookmarksByUserId(@RequestParam("userId", required = true) userId: Int): Flow<BookResponse> {
        return bookService.findBookmarksByUserId(userId)
    }

    @GetMapping("/bookmarks/add")
    suspend fun addOrRemoveBookInBookmark(
        @RequestParam("bookId") bookId: Int,
        @RequestParam("userId") userId: Int,
    ): Boolean {
        return bookService.addOrRemoveBookInBookmark(bookId = bookId, userId = userId)
    }

}