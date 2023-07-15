package com.job.cm.libraryapp.service

import com.job.cm.libraryapp.exception.ApiException
import com.job.cm.libraryapp.model.user.User
import com.job.cm.libraryapp.model.user.UserStatus
import com.job.cm.libraryapp.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    suspend fun login(userLogin: User.UserLogin): Int {
        if (!userRepository.existsByEmail(userLogin.username)) {
            throw ApiException("Utilizador nao existe!")
        }
        val user = userRepository.findUserByEmailAndPassword(userLogin.username, userLogin.password)
            ?: throw ApiException("Password Invalida!")

        if (user.status == UserStatus.Cancelled) throw ApiException("Conta cancelada!")

        return user.id!!
    }

    suspend fun signUp(user: User): Boolean {
        if (userRepository.existsByEmail(user.email)) {
            throw ApiException("Email em uso!")
        }
        userRepository.save(user)
        return true
    }

    suspend fun blockUser(userId: Int): String {
        val user = userRepository.findById(userId)!!
        val blockedUser = userRepository.save(entity = user.copy(status = UserStatus.Blocked))
        blockedUser.firstname + " " + blockedUser.lastname
        return "Usuario ${blockedUser.firstname} ${blockedUser.lastname} foi bloqueado"
    }

    suspend fun findById(userId: Int): User {
        return userRepository.findById(userId) ?: throw ApiException("User not found")
    }
}