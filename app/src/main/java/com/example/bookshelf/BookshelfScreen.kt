package com.example.bookshelf

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun BookshelfApp(
    viewModel: BookshelfViewModel = viewModel(factory = BookshelfViewModel.Factory)
) {
    Scaffold { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (val state = viewModel.uiState) {
                is BookshelfUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                is BookshelfUiState.Error ->
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error al cargar los libros")
                        Button(onClick = { viewModel.getBooks("jazz history") }) {
                            Text("Reintentar")
                        }
                    }
                is BookshelfUiState.Success -> BooksGrid(state.books)
            }
        }
    }
}

@Composable
fun BooksGrid(books: List<Book>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books, key = { it.id }) { book ->
            Card {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://"))
                        .crossfade(true)
                        .build(),
                    contentDescription = book.volumeInfo?.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.7f)
                )
            }
        }
    }
}