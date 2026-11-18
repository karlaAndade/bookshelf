package com.example.bookshelf

interface BooksRepository {
    suspend fun getBooks(query: String): List<Book>
}

class NetworkBooksRepository(private val api: BooksApiService) : BooksRepository {
    override suspend fun getBooks(query: String): List<Book> {
        return api.searchBooks(query).items.orEmpty()
    }
}