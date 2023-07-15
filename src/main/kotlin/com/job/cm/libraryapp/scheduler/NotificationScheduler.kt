package com.job.cm.libraryapp.scheduler

import com.job.cm.libraryapp.repository.LoanRepository
import com.job.cm.libraryapp.service.EmailService
import com.job.cm.libraryapp.service.UserService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDateTime

@Configuration
@EnableScheduling
class NotificationScheduler(
    private val loanRepository: LoanRepository,
    private val emailService: EmailService,
    private val userService: UserService,
) {

    @Scheduled(fixedDelay = 5000)
    fun schedule() {
        CoroutineScope(Dispatchers.IO).launch {
            loanRepository.findAll()
                .filter { it.returnDate.isAfter(LocalDateTime.now().plusDays(2)) }
                .map {
                    val user = userService.findById(it.userId)
                    emailService.sendEmail(
                        to = user.email,
                        subject = "Biblioteca GRUPO 9 - Booking",
                        content = "Faltam 2 dias para devolveres o livro #${it.bookId}! Seja rapido😂😂😂😂"
                    )
                }
        }
    }
}