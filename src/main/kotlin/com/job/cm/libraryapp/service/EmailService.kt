package com.job.cm.libraryapp.service

import com.job.cm.libraryapp.exception.ApiException
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture


@Service
class EmailService(
    private val javaMailSender: JavaMailSender,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value(value = "\${email.from}")
    private val myEmail: String? = null

    suspend fun sendEmail(
        to: String,
        subject: String,
        content: String,
    ) = withContext(Dispatchers.IO) {
        try {
            println("Email sending....")
            val sampleEmail = SimpleMailMessage()

            sampleEmail.setSubject(subject)
            sampleEmail.setFrom(myEmail!!)
            sampleEmail.setTo(to)
            sampleEmail.setText(content)
            javaMailSender.send(sampleEmail)
            println("Email send to : $to")
        } catch (e: Exception) {
            e.printStackTrace()
            logger.info("An error occurred while sending the email to $to")
        }
    }

    fun sendSamplePasswordToEmployee(
        to: String,
        password: String,
    ) {
        CompletableFuture.runAsync {
            try {
                logger.info("Sending email... thread={${Thread.currentThread().name}}")
                val sampleEmail = SimpleMailMessage()
                sampleEmail.setSubject("")
                sampleEmail.setFrom(myEmail!!)
                sampleEmail.setTo(to)
                sampleEmail.setText("Your password is $password")
                javaMailSender.send(sampleEmail)
                logger.info("Email send to [$to] completed   thread={${Thread.currentThread().name}}")
            } catch (e: Exception) {
                e.printStackTrace()
                logger.error("An error occurred while sending email to $to     thread={${Thread.currentThread().name}}")
                //throw ApiException("An error occurred while sending the email to $to")
            }
        }
        logger.info("Sending email end line::   thread={${Thread.currentThread().name}}")
    }


    suspend fun sendEmailWithHtmlBody(to: String, subject: String, htmlBody: String) = withContext(Dispatchers.IO) {
        try {
            val message: MimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlBody, true)
            javaMailSender.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}