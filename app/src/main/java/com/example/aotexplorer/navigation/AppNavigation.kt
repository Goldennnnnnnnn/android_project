package com.example.aotexplorer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.aotexplorer.ui.CharacterDetailScreen
import com.example.aotexplorer.ui.CharacterListScreen
import com.example.aotexplorer.ui.TitansScreen
import com.example.aotexplorer.viewmodel.CharacterViewModel

@Composable
fun AppNavigation(viewModel: CharacterViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Characters") },
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.CHARACTER_LIST } == true,
                    onClick = {
                        navController.navigate(Routes.CHARACTER_LIST) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Face, contentDescription = null) },
                    label = { Text("Titans") },
                    selected = currentDestination?.hierarchy?.any { it.route == Routes.TITANS } == true,
                    onClick = {
                        navController.navigate(Routes.TITANS) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.CHARACTER_LIST,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.CHARACTER_LIST) {
                CharacterListScreen(
                    viewModel = viewModel,
                    onCharacterClick = { characterId ->
                        navController.navigate(Routes.characterDetail(characterId))
                    }
                )
            }
            composable(
                route = Routes.CHARACTER_DETAIL,
                arguments = listOf(
                    navArgument("characterId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
                CharacterDetailScreen(
                    characterId = characterId,
                    viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Routes.TITANS) {
                TitansScreen(viewModel = viewModel)
            }
        }
    }
}
