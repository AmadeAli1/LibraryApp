package com.job.cm.libraryapp.controller

import com.job.cm.libraryapp.model.user.User
import com.job.cm.libraryapp.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/login")
    suspend fun login(
        @RequestParam("user") username: String,
        @RequestParam("password") password: String,
    ): Int {
        return userService.login(User.UserLogin(username, password))
    }

    @PostMapping("/signUp")
    suspend fun signUp(@Valid @RequestBody user: User): Boolean {
        return userService.signUp(user)
    }

}