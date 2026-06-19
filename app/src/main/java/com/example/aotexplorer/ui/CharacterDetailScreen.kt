package com.example.aotexplorer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.aotexplorer.model.CharacterDetailUiState
import com.example.aotexplorer.viewmodel.CharacterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int,
    viewModel: CharacterViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.getCharacterById(characterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (val state = uiState) {
                is CharacterDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CharacterDetailUiState.Success -> {
                    val character = state.character
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = character.img,
                            contentDescription = character.name,
                            modifier = Modifier.size(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = character.name, style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        DetailItem("Alias", character.alias?.joinToString(", ") ?: "Inconnu")
                        DetailItem("Species", character.species?.joinToString(", ") ?: "Inconnu")
                        DetailItem("Gender", character.gender ?: "Inconnu")
                        DetailItem("Age", character.age ?: "Inconnu")
                        DetailItem("Birth Place", character.birthplace ?: "Inconnu")
                        DetailItem("Residence", character.residence ?: "Inconnu")
                        DetailItem("Status", character.status ?: "Inconnu")
                        DetailItem("Occupation", character.occupation ?: "Inconnu")
                        DetailItem("Regiment", character.regiment ?: character.roles?.joinToString(", ") ?: "Inconnu")
                    }
                }
                is CharacterDetailUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.getCharacterById(characterId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
    }
}
