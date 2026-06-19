package com.example.aotexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aotexplorer.model.*
import com.example.aotexplorer.network.RetrofitClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Loading)
    val uiState: StateFlow<CharacterUiState> = _uiState

    private val _detailUiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val detailUiState: StateFlow<CharacterDetailUiState> = _detailUiState

    private val _titansUiState = MutableStateFlow<TitanUiState>(TitanUiState.Loading)
    val titansUiState: StateFlow<TitanUiState> = _titansUiState

    // Search and Filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedStatus = MutableStateFlow<String?>(null)
    val selectedStatus: StateFlow<String?> = _selectedStatus

    // Theme state: null = system, true = dark, false = light
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme: StateFlow<Boolean?> = _isDarkTheme

    private var allCharacters: List<Character> = emptyList()

    init {
        getCharacters()
        getTitans()
    }

    fun toggleTheme() {
        _isDarkTheme.value = when (_isDarkTheme.value) {
            null -> true // If system, force dark
            true -> false // If dark, force light
            false -> null // If light, back to system
        }
    }

    fun getCharacters() {
        viewModelScope.launch {
            _uiState.value = CharacterUiState.Loading
            try {
                val response = RetrofitClient.apiService.getCharacters()
                allCharacters = response.results
                filterCharacters()
            } catch (e: Exception) {
                _uiState.value = CharacterUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun getTitans() {
        viewModelScope.launch {
            _titansUiState.value = TitanUiState.Loading
            try {
                val response = RetrofitClient.apiService.getTitans()
                _titansUiState.value = TitanUiState.Success(response.results)
            } catch (e: Exception) {
                _titansUiState.value = TitanUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterCharacters()
    }

    fun onStatusSelected(status: String?) {
        _selectedStatus.value = if (_selectedStatus.value == status) null else status
        filterCharacters()
    }

    private fun filterCharacters() {
        val filtered = allCharacters.filter { character ->
            val matchesSearch = character.name.contains(_searchQuery.value, ignoreCase = true)
            val matchesStatus = _selectedStatus.value == null || 
                              character.status?.equals(_selectedStatus.value, ignoreCase = true) == true
            matchesSearch && matchesStatus
        }
        _uiState.value = CharacterUiState.Success(filtered)
    }

    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            _detailUiState.value = CharacterDetailUiState.Loading
            try {
                val character = RetrofitClient.apiService.getCharacterById(id)
                _detailUiState.value = CharacterDetailUiState.Success(character)
            } catch (e: Exception) {
                _detailUiState.value = CharacterDetailUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
