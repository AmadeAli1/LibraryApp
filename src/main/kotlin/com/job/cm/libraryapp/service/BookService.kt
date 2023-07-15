package com.job.cm.libraryapp.service

import com.job.cm.libraryapp.exception.ApiException
import com.job.cm.libraryapp.model.book.Book
import com.job.cm.libraryapp.model.book.BookResponse
import com.job.cm.libraryapp.model.book.Bookmark
import com.job.cm.libraryapp.model.book.booking.Booking
import com.job.cm.libraryapp.model.book.loan.BookLoaned
import com.job.cm.libraryapp.model.book.loan.BooksWithLoan
import com.job.cm.libraryapp.repository.BookRepository
import com.job.cm.libraryapp.repository.BookingRepository
import com.job.cm.libraryapp.repository.BookmarkRepository
import com.job.cm.libraryapp.repository.LoanRepository
import com.job.cm.libraryapp.scheduler.LoanScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val loanRepository: LoanRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val loanScheduler: LoanScheduler,
    private val bookingRepository: BookingRepository,
    private val emailService: EmailService,
    private val userService: UserService,
) {

    suspend fun postBook(book: Book): Boolean {
        if (bookRepository.findBookByIsbn(book.isbn) != null) {
            throw ApiException("O livro ja existe!")
        }
        bookRepository.save(book)
        return true
    }

    suspend fun findAll(): Flow<BookResponse> {
        return bookRepository.findAll().map {
            it.toResponse()
        }
    }

    suspend fun findAllAvailableBooks(): Flow<BookResponse> {
        return bookRepository.findAllByAvailable(true).map {
            it.toResponse()
        }
    }

    suspend fun findBookById(id: Int): BookResponse {
        return bookRepository.findById(id)?.toResponse() ?: throw ApiException("Book not found")
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
        notifyAll(bookId)
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
        return bookmarkRepository.findAllByUserId(userId).map(Book::toResponse)
    }

    suspend fun search(query: String): Flow<BookResponse> {
        if (query.isBlank()) findAll()
        return bookRepository.search(query).map(Book::toResponse)
    }

    suspend fun searchByRating(rating: Int): Flow<BookResponse> {
        return bookRepository.findAllByRating(rating).map(Book::toResponse)
    }

    suspend fun addBooking(userId: Int, bookId: Int): Boolean {
        val booking = bookingRepository.findAllByUserIdAndBookIdOrderById(
            userId = userId,
            bookId = bookId,
        )

        if (booking.count() == 0) {
            bookingRepository.save(entity = Booking(userId = userId, bookId = bookId))
            return true
        }

        val all = booking.toList().all { it.status == Booking.Status.Reservado }
        if (all) {
            bookingRepository.save(entity = Booking(userId = userId, bookId = bookId))
            return true
        }
        throw ApiException("Ja efectou um booking para esse livro!")
    }

    suspend fun notifyAll(bookId: Int) {
        bookingRepository
            .findAllByBookIdAndStatusEqualsOrderById(bookId, Booking.Status.Reservado)
            .map { bookingRepository.save(it.copy(status = Booking.Status.Concluido)) }
            .flowOn(Dispatchers.IO)
            .map { userService.findById(it.userId) }
            .collect {
                emailService.sendEmail(
                    to = it.email,
                    subject = "Biblioteca GRUPO 9 - Booking",
                    content = "O livro ja #$bookId esta disponivel para alugar novamente! Seja rapido😂😂😂😂"
                )
            }
    }


}