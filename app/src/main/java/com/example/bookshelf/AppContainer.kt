package com.example.bookshelf

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val booksRepository: BooksRepository
}

class DefaultAppContainer : AppContainer {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val url = chain.request().url.newBuilder()
                .addQueryParameter("key", "AIzaSyAlXfR26lBXxe3pKYWOEKoRv2j9Wqqy0QA")
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }

    override val booksRepository: BooksRepository by lazy {
        NetworkBooksRepository(service)
    }
}