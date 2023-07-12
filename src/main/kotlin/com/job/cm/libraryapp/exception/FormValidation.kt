package com.job.cm.libraryapp.exception

import jakarta.validation.Validator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class FormValidation(
    private val validator: Validator,
) {
    fun <T> validateRequest(request: T): ResponseEntity<out Any>? {
        val validate = validator.validate(request)
        if (validate.isNotEmpty()) {
            val errors = validate.map { ApiMessage(field = it.propertyPath.toString(), message = it.message) }
            return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
        }
        return null
    }

}