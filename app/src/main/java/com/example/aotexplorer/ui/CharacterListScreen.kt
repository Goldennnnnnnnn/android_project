package com.example.aotexplorer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aotexplorer.model.CharacterUiState
import com.example.aotexplorer.ui.components.CharacterCard
import com.example.aotexplorer.viewmodel.CharacterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    viewModel: CharacterViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val isDarkThemeOverride by viewModel.isDarkTheme.collectAsState()

    val statusOptions = listOf("Alive", "Deceased", "Unknown")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AoT Explorer") },
                actions = {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Icon(
                            imageVector = when (isDarkThemeOverride) {
                                true -> Icons.Default.Brightness7
                                false -> Icons.Default.Brightness4
                                null -> Icons.Default.Brightness4 // Default icon for system
                            },
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                placeholder = { Text("Search character...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            // Status Filter Chips
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(statusOptions) { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { viewModel.onStatusSelected(status) },
                        label = { Text(status) }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (val state = uiState) {
                    is CharacterUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is CharacterUiState.Success -> {
                        LazyColumn {
                            items(state.characters) { character ->
                                CharacterCard(character = character) {
                                    onCharacterClick(character.id)
                                }
                            }
                        }
                    }
                    is CharacterUiState.Error -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = state.message, color = MaterialTheme.colorScheme.error)
                            Button(onClick = { viewModel.getCharacters() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}
