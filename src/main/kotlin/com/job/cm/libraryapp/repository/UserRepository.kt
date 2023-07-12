@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.job.cm.libraryapp.repository

import com.job.cm.libraryapp.model.user.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CoroutineCrudRepository<User, Int> {

    suspend fun existsByEmail(email: String): Boolean

    suspend fun findUserByEmailAndPassword(email: String, password: String): User?

}