package com.example.ui.more

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(viewModel: MainViewModel) {
    val sources by viewModel.sourcesList.collectAsState()
    val labels by viewModel.labelsList.collectAsState()
    val logs by viewModel.logsList.collectAsState()
    val isDark by viewModel.isDarkTheme.collectAsState()

    var selectedSection by remember { mutableStateOf("Menu") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedSection == "Menu") "More & Settings" else "AI Khobor24 - $selectedSection") },
                navigationIcon = {
                    if (selectedSection != "Menu") {
                        IconButton(onClick = { selectedSection = "Menu" }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (selectedSection) {
                "Menu" -> {
                    MoreMenuItem("Sources & RSS Feeds", Icons.Default.RssFeed) { selectedSection = "Sources" }
                    MoreMenuItem("Categories & Blogger Labels", Icons.Default.Label) { selectedSection = "Labels" }
                    MoreMenuItem("System Logs & Errors", Icons.Default.ReceiptLong) { selectedSection = "Logs" }
                    MoreMenuItem("Settings & Theme", Icons.Default.Settings) { selectedSection = "Settings" }
                    MoreMenuItem("Backup & Export", Icons.Default.Backup) { selectedSection = "Backup" }
                    MoreMenuItem("About AI Khobor24", Icons.Default.Info) { selectedSection = "About" }
                }
                "Sources" -> {
                    Text("RSS News Sources", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(sources) { source ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(source.name, fontWeight = FontWeight.Bold)
                                    Text(source.url, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Badge { Text(source.category) }
                                        Text("Interval: ${source.intervalMin}m", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
                "Labels" -> {
                    Text("Synced Blogger Labels", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(labels) { label ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(label.label, fontWeight = FontWeight.Bold)
                                        Text("Posts: ${label.postCount} | Last seen: ${label.lastSeen}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    Badge(containerColor = if (label.enabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer) {
                                        Text(if (label.enabled) "Active" else "Disabled")
                                    }
                                }
                            }
                        }
                    }
                }
                "Logs" -> {
                    Text("System Execution Logs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(logs) { log ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text(log.module, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        Text(log.timestamp, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(log.message, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
                "Settings" -> {
                    Text("App Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dark Theme", fontWeight = FontWeight.Medium)
                            Switch(
                                checked = isDark,
                                onCheckedChange = { viewModel.toggleTheme() },
                                modifier = Modifier.testTag("switch_dark_theme")
                            )
                        }
                    }
                }
                "Backup" -> {
                    Text("Backup & Database", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Google Sheets Database Backup", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("All state is stored securely in your connected Google Spreadsheet and Script Properties.")
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = {}) {
                                Text("Export Backup JSON")
                            }
                        }
                    }
                }
                "About" -> {
                    Text("About AI Khobor24", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("AI Khobor24 v1.0", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Automated Bengali/English AI news pipeline controller for aikhobor24.blogspot.com.")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Powered by Gemini API, Google Apps Script, and Blogger API v3.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoreMenuItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyLarge)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}
