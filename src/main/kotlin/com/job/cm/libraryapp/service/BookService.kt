package com.job.cm.libraryapp.service

import com.job.cm.libraryapp.exception.ApiException
import com.job.cm.libraryapp.model.book.Book
import com.job.cm.libraryapp.model.book.BookGenre
import com.job.cm.libraryapp.model.book.BookResponse
import com.job.cm.libraryapp.model.book.Bookmark
import com.job.cm.libraryapp.model.book.loan.BookLoaned
import com.job.cm.libraryapp.model.book.loan.BooksWithLoan
import com.job.cm.libraryapp.repository.BookRepository
import com.job.cm.libraryapp.repository.BookmarkRepository
import com.job.cm.libraryapp.repository.LoanRepository
import com.job.cm.libraryapp.scheduler.LoanScheduler
import com.job.cm.libraryapp.utils.toByteArray
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val loanRepository: LoanRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val environment: Environment,
    private val loanScheduler: LoanScheduler,
) {
    private var baseUrl = environment["baseUrl"]!!

    suspend fun postBook(book: Book, image: FilePart): Boolean {
        if (bookRepository.findBookByIsbn(book.isbn) != null) {
            throw ApiException("O livro ja existe!")
        }
        val img = image.toByteArray()
        book.image = img
        if (book.type != BookGenre.Academic) {
            book.subType = null
        }
        bookRepository.save(book)
        return true
    }

    suspend fun findAll(): Flow<BookResponse> {
        return bookRepository.findAll().map {
            it.toResponse(baseUrl)
        }
    }

    suspend fun findAllAvailableBooks(): Flow<BookResponse> {
        return bookRepository.findAllByAvailable(true).map {
            it.toResponse(baseUrl)
        }
    }

    suspend fun findBookById(id: Int): BookResponse {
        return bookRepository.findById(id)?.toResponse(baseUrl) ?: throw ApiException("Book not found")
    }

    suspend fun downloadBookImage(id: Int): ByteArray {
        return bookRepository.findById(id)?.image ?: throw ApiException("Book not found!")
    }

    /**
     * Somente atributo bookId,userId,status
     */
    suspend fun loanBook(bookLoaned: BookLoaned): Boolean {
        val loaned = loanRepository.findByBookIdAndUserIdAndStatusEquals(
            bookId = bookLoaned.bookId,
            userId = bookLoaned.userId,
            status = BookLoaned.LoanStatus.NotDelivered
        )
        if (loaned == null) {
            val book = bookRepository.findById(bookLoaned.bookId)!!
            if (book.quantity > 0) {
                val savedBookLoan = loanRepository.save(entity = bookLoaned)
                val quantity = book.quantity - 1
                bookRepository.save(entity = book.copy(available = quantity > 0, quantity = quantity))
                loanScheduler.schedule(savedBookLoan)
                return true
            }
            throw ApiException("O livro nao esta disponivel!")
        }
        throw ApiException("O Sr/Sra ainda nao devolveu o livro!")
    }

    suspend fun deliveryBook(userId: Int, bookId: Int): Boolean {
        val loaned = loanRepository.findByBookIdAndUserIdAndStatusEquals(
            bookId = bookId,
            userId = userId,
            status = BookLoaned.LoanStatus.NotDelivered
        ) ?: throw ApiException("O usuario nao contem o livro para devolucao!")
        val book = bookRepository.findById(loaned.bookId) ?: throw ApiException("Livro nao encondrado")
        bookRepository.save(entity = book.copy(quantity = book.quantity + 1, available = true))
        loanRepository.save(entity = loaned.copy(status = BookLoaned.LoanStatus.Delivered))
        return true
    }

    suspend fun findAllBooksWithLoan(userId: Int): Flow<BooksWithLoan> {
        return loanRepository.findAllBooksWithLoan(userId)
    }


    suspend fun addOrRemoveBookInBookmark(bookId: Int, userId: Int): Boolean {
        if (bookmarkRepository.existsBookmarkByBookIdAndUserId(bookId, userId)) {
            return bookmarkRepository.deleteBookmarkByBookIdAndUserId(bookId, userId) > 0
        }
        bookmarkRepository.save(Bookmark(userId = userId, bookId = bookId))
        return true
    }

    suspend fun findBookmarksByUserId(userId: Int): Flow<BookResponse> {
        return bookmarkRepository.findAllByUserId(userId).map { it.toResponse(baseUrl) }
    }


}