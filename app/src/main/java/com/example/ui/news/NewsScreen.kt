package com.example.ui.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.NewsItem
import com.example.ui.main.MainViewModel

@Composable
fun NewsScreen(viewModel: MainViewModel) {
    val newsList by viewModel.newsList.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "West Bengal", "World", "Technology", "Sports")

    var selectedNews by remember { mutableStateOf<NewsItem?>(null) }

    val filteredNews = if (selectedCategory == "All") newsList else newsList.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Collected News Sources", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Category filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) },
                    modifier = Modifier.testTag("filter_chip_$cat")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredNews) { item ->
                NewsCard(
                    item = item,
                    onGenerate = { viewModel.generateArticle(item.id) },
                    onClick = { selectedNews = item }
                )
            }
        }
    }

    if (selectedNews != null) {
        AlertDialog(
            onDismissRequest = { selectedNews = null },
            title = { Text(selectedNews!!.title) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Category: ${selectedNews!!.category}", fontWeight = FontWeight.Bold)
                    Text("Snippet: ${selectedNews!!.snippet}")
                    if (selectedNews!!.researchSummary != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("AI Research Summary:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(selectedNews!!.researchSummary!!)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.generateArticle(selectedNews!!.id)
                    selectedNews = null
                }) {
                    Text("Generate Article")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedNews = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun NewsCard(item: NewsItem, onGenerate: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("news_card_${item.id}"),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Badge { Text(item.category) }
                Text(item.publishedAt.take(10), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.snippet, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onGenerate,
                    modifier = Modifier.testTag("generate_from_news_${item.id}")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Generate Article")
                }
            }
        }
    }
}
