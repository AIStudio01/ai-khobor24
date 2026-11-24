package com.example.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.articles.ArticlesScreen
import com.example.ui.automation.AutomationScreen
import com.example.ui.home.HomeScreen
import com.example.ui.more.MoreScreen
import com.example.ui.news.NewsScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object News : Screen("news", "News", Icons.Default.Article)
    object Articles : Screen("articles", "Articles", Icons.Default.EditNote)
    object Automation : Screen("automation", "Automation", Icons.Default.SmartToy)
    object More : Screen("more", "More", Icons.Default.Menu)
}

@Composable
fun MainContainerScreen(viewModel: MainViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val items = listOf(Screen.Home, Screen.News, Screen.Articles, Screen.Automation, Screen.More)

    Scaffold(
        bottomBar = {
            NavigationBar(modifier = Modifier.testTag("bottom_navigation")) {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        modifier = Modifier.testTag("nav_item_${screen.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                is Screen.Home -> HomeScreen(viewModel)
                is Screen.News -> NewsScreen(viewModel)
                is Screen.Articles -> ArticlesScreen(viewModel)
                is Screen.Automation -> AutomationScreen(viewModel)
                is Screen.More -> MoreScreen(viewModel)
            }
        }
    }
}
