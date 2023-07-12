package com.job.cm.libraryapp.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.multipart.FilePart

suspend fun FilePart.toByteArray(): ByteArray = withContext(Dispatchers.IO) {
    DataBufferUtils.join(this@toByteArray.content()).map { it.asByteBuffer().array() }.block()!!
}