package com.example.aotexplorer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.aotexplorer.model.Titan
import com.example.aotexplorer.model.TitanUiState
import com.example.aotexplorer.viewmodel.CharacterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitansScreen(viewModel: CharacterViewModel) {
    val uiState by viewModel.titansUiState.collectAsState()
    val isDarkThemeOverride by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("The Nine Titans") },
                actions = {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Icon(
                            imageVector = when (isDarkThemeOverride) {
                                true -> Icons.Default.Brightness7
                                false -> Icons.Default.Brightness4
                                null -> Icons.Default.Brightness4
                            },
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (val state = uiState) {
                is TitanUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is TitanUiState.Success -> {
                    LazyColumn {
                        items(state.titans) { titan ->
                            TitanCard(titan)
                        }
                    }
                }
                is TitanUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.getTitans() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TitanCard(titan: Titan) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = titan.img,
                contentDescription = titan.name,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = titan.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Current Inheritor: ${titan.current_inheritor ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Abilities:", style = MaterialTheme.typography.titleSmall)
            Text(text = titan.abilities?.joinToString(", ") ?: "None", style = MaterialTheme.typography.bodySmall)
        }
    }
}
