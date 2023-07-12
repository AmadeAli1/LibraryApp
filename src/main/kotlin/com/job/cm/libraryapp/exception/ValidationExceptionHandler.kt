package com.job.cm.libraryapp.exception

import com.job.cm.libraryapp.exception.ApiMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException

@ControllerAdvice
class ValidationExceptionHandler {

    @ExceptionHandler(WebExchangeBindException::class)
    suspend fun isValid(e: WebExchangeBindException): ResponseEntity<List<ApiMessage>> {
        val errors = e.bindingResult.allErrors.stream().map {
            lateinit var error: ApiMessage
            if (it is FieldError) {
                error = ApiMessage(field = it.field, message = it.defaultMessage!!)
            }
            error
        }.toList()
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

}