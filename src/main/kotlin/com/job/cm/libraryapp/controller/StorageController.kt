package com.job.cm.libraryapp.controller

import com.job.cm.libraryapp.exception.ApiException
import com.job.cm.libraryapp.service.BookService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/api/storage")
@RestController
class StorageController(
    private val bookService: BookService,
) {

    @GetMapping("/book/image/download")
    suspend fun getImage(
        @RequestParam("id", required = true) id: Int?,
    ): ResponseEntity<Resource> {
        if (id == null) throw ApiException("Query param <id> malformed!, required book id")
        return withContext(Dispatchers.IO) {
            val image = bookService.downloadBookImage(id = id)
            ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_TYPE)
                .body(ByteArrayResource(image))
        }
    }

}