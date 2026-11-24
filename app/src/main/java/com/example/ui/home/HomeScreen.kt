package com.example.ui.home

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
import com.example.ui.main.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val newsList by viewModel.newsList.collectAsState()
    val articlesList by viewModel.articlesList.collectAsState()
    val logs by viewModel.logsList.collectAsState()

    val collectedCount = newsList.count { it.status == "Collected" }
    val researchedCount = newsList.count { it.status == "Research Done" }
    val articleReadyCount = articlesList.count { it.status == "Article Generated" }
    val draftSavedCount = articlesList.count { it.status == "Draft Saved" }
    val scheduledCount = articlesList.count { it.status == "Scheduled" }
    val publishedCount = articlesList.count { it.status == "Published" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("AI Khobor24 Dashboard", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("aikhobor24.blogspot.com", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { viewModel.collectNewsNow() }, modifier = Modifier.testTag("refresh_home_button")) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$publishedCount", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Published", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("3", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Today", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$draftSavedCount", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Drafts", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        item {
            Text("Pipeline Status Counts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCard("Collected", "$collectedCount", Modifier.weight(1f))
                StatusCard("Researched", "$researchedCount", Modifier.weight(1f))
                StatusCard("Ready", "$articleReadyCount", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCard("Drafts", "$draftSavedCount", Modifier.weight(1f))
                StatusCard("Scheduled", "$scheduledCount", Modifier.weight(1f))
                StatusCard("Published", "$publishedCount", Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.collectNewsNow() },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("collect_now_button")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Collect Now")
                }
                Button(
                    onClick = {
                        if (newsList.isNotEmpty()) {
                            viewModel.generateArticle(newsList.first().id)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("generate_articles_button")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Generate Article")
                }
            }
        }

        item {
            Text("Recent Pipeline Logs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        items(logs.take(5)) { log ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("[${log.module}] ${log.message}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        Text(log.timestamp, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                    Badge { Text(log.level) }
                }
            }
        }
    }
}

@Composable
fun StatusCard(title: String, count: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(count, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, style = MaterialTheme.typography.bodySmall)
        }
    }
}
