package com.example.aotexplorer.model

sealed class TitanUiState {
    object Loading : TitanUiState()
    data class Success(val titans: List<Titan>) : TitanUiState()
    data class Error(val message: String) : TitanUiState()
}
