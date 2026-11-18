package com.example.bookshelf

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeBooksApiService : BooksApiService {
    override suspend fun searchBooks(query: String) = SearchResponse(
        listOf(
            Book("1", VolumeInfo("Libro 1", ImageLinks("https://example.com/1.jpg"))),
            Book("2", VolumeInfo("Libro 2", ImageLinks("https://example.com/2.jpg")))
        )
    )

    override suspend fun getBook(id: String) =
        Book(id, VolumeInfo("Libro $id", ImageLinks("https://example.com/$id.jpg")))
}

class NetworkBooksRepositoryTest {
    @Test
    fun getBooks_devuelveLibrosDelServicio() = runTest {
        val repo = NetworkBooksRepository(FakeBooksApiService())
        val books = repo.getBooks("cualquiera")
        assertEquals(2, books.size)
        assertEquals("Libro 1", books[0].volumeInfo?.title)
    }
}