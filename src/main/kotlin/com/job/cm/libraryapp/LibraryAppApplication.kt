package com.job.cm.libraryapp

import com.job.cm.libraryapp.repository.LoanRepository
import com.job.cm.libraryapp.scheduler.LoanScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryAppApplication(
    private val loanScheduler: LoanScheduler,
    private val bookLoans: LoanRepository,
) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            logger.info("Start LoanScheduler")
            bookLoans.findAll().collect(loanScheduler::schedule)
            logger.info("----------------------------------------------------")
        }
    }

}

fun main(args: Array<String>) {
    runApplication<LibraryAppApplication>(*args)
}
