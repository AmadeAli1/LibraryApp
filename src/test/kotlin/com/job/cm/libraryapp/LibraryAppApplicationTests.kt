package com.job.cm.libraryapp

import com.job.cm.libraryapp.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LibraryAppApplicationTests @Autowired constructor(
    private val userRepository: UserRepository
) {

    @Test
    fun contextLoads() {

    }

}
