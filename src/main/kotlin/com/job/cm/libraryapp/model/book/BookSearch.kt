package com.job.cm.libraryapp.model.book

data class BookSearch(
    val year: Int? = null,
    val title: String? = null,
    val author: String? = null,
    val isbn: String? = null,
    val type: BookGenre? = null,
    val language: BookLanguage? = null,
    val available: Boolean = true,
    val ratings: Int? = null,
    val subType: BookGenre.SubType? = null,
) {

    fun generateQuery(): String {
        val bookSearchClass = this.javaClass
        val query = bookSearchClass.declaredFields
            .filter {
                it.isAccessible = true
                it.get(this) != null
            }.map {
                "${it.name}=${it.get(this)}"
            }.joinToString(prefix = "select * from books where ", separator = " and ")
        return query
    }

}
