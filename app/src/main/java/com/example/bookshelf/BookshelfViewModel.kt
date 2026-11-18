package com.example.bookshelf

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookshelfUiState {
    data class Success(val books: List<Book>) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}

class BookshelfViewModel(private val repository: BooksRepository) : ViewModel() {

    var uiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init { getBooks("jazz history") }

    fun getBooks(query: String) {
        viewModelScope.launch {
            uiState = BookshelfUiState.Loading
            uiState = try {
                BookshelfUiState.Success(repository.getBooks(query))
            } catch (e: Exception) {
                android.util.Log.e("Bookshelf", "Fallo la carga", e)
                BookshelfUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as BookshelfApplication
                BookshelfViewModel(app.container.booksRepository)
            }
        }
    }
}