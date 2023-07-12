package com.job.cm.libraryapp.model.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    @Id
    @Column
    val id: Int? = null,
    @Column("FIRST_NAME")
    @field:NotBlank
    @field:Length(min = 2)
    val firstname: String,
    @Column("LAST_NAME")
    @field:NotBlank
    @field:Length(min = 2)
    val lastname: String,
    @Column("GENDER")
    @field:NotNull
    val gender: UserGenre,
    @Column("CONTACT")
    @field:Length(max = 12, min = 12)
    @field:Pattern(regexp = "^(25884|25885|25882|25887)[0-9]+$")
    val contact: String,
    @Column("ADDRESS")
    @field:NotBlank @field:Length(min = 6)
    val address: String,
    @Column("EMAIL")
    @field:Email @field:NotBlank
    val email: String,
    @Column("DOC_TYPE")
    @field:NotNull
    val docType: UserDocType,
    @Column("DOC_NUMBER")
    @field:Length(max = 20, min = 8)
    val docNumber: String,
    @Column("PASSWORD")
    @field:NotBlank @field:Length(min = 6)
    val password: String,
    @Column("STATUS")
    @field:NotNull
    val status: UserStatus,
) {

    public data class UserLogin(
        @field:Email @field:NotBlank val username: String,
        @field:NotBlank @field:Length(min = 6) val password: String,
    )

}
