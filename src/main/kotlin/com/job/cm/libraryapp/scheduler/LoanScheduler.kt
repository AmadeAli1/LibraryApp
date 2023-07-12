package com.job.cm.libraryapp.scheduler

import com.job.cm.libraryapp.model.book.loan.BookLoaned
import com.job.cm.libraryapp.model.user.UserStatus
import com.job.cm.libraryapp.service.UserService
import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.schedule

@Component
@Lazy
class LoanScheduler(
    private val userService: UserService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun schedule(bookLoaned: BookLoaned) {
        val schedule = {
            mono {
                userService.blockUser(bookLoaned.userId)
            }.doOnSuccess {
                logger.info(it)
            }.subscribe()
        }
        val endDateTask = bookLoaned.returnDate.plusDays(1)
        if (endDateTask.isBefore(LocalDateTime.now())) {
            schedule()
        } else {
            Timer().schedule(time = endDateTask.toDate()) {
                logger.info("Task for userId = ${bookLoaned.userId} started}")
                schedule()
                this.cancel()
            }
        }
    }


    private fun LocalDateTime.toDate(): Date {
        return Calendar.getInstance().also {
            it.set(year, monthValue - 1, dayOfMonth, hour, minute, second)
        }.run {
            Date(this.timeInMillis)
        }
    }
}